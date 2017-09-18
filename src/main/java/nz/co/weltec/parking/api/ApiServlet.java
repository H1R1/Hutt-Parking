package nz.co.weltec.parking.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.net.MediaType;
import com.google.gson.Gson;

import nz.co.weltec.parking.json.JSON;

public abstract class ApiServlet extends HttpServlet {

	private static final long serialVersionUID = -2565751359049209607L;

	/**
	 * Optional interface for implementers to provide their own {@link Gson}
	 * definitions.
	 * 
	 * By default, {@link JSON#gsonLowercaseUnderscores()} is the {@link Gson} provider
	 * 
	 */
	public interface GsonProvider {
		Gson gson();
	}

	@SuppressWarnings("unused")
	private static final class Response {

		private static final class Error {

			private String error;

			private Error(String error) {
				this.error = error;
			}
		}

		private int status;
		private boolean success;
		private Object payload;

		private Response(Object payload, boolean success, int status) {
			this.status = status;
			this.payload = payload;
			this.success = success;
		}

	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) { 
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Allow-Methods", "GET");
		resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
	}

	protected String getAuthorisationToken(HttpServletRequest request) {
		try {
			return request.getHeader("Authorization").replace("Bearer ", "").trim();
		} catch (Exception e) {
			return null;
		}
	}

	protected boolean hasAuthorisationToken(HttpServletRequest request) {
		String token = getAuthorisationToken(request);
		return token != null && StringUtils.trimToEmpty(token).length() > 0;
	}

	protected MediaType getAcceptType(HttpServletRequest request) {
		try {
			return MediaType.parse(request.getHeader("Accept"));
		} catch (Exception e) {
			return MediaType.PLAIN_TEXT_UTF_8;
		}
	}

	protected MediaType getContentType(HttpServletRequest request) {
		try {
			return MediaType.parse(request.getContentType());
		} catch (Exception e) {
			return getAcceptType(request);
		}
	}

	protected Long getContentLength(HttpServletRequest request) throws Exception {
		Long contentLength = null;
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			if (headerName.replace("-", "").replace(" ", "").toLowerCase().equalsIgnoreCase("contentlength")) {
				contentLength = Long.parseLong(request.getHeader(headerName));
				break;
			}
		}
		if (contentLength == null) {
			throw new IllegalArgumentException("No defined content length");	
		}
		else {
			return contentLength;
		}
	}

	protected String getUserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	protected String getBody(HttpServletRequest request) throws IOException {
		return IOUtils.toString(request.getReader());
	}

	protected boolean containsNumberParameters(HttpServletRequest req) {
		List<Number> id = getNumberParameters(req);
		return id != null && id.size() > 0;
	}

	protected List<Number> getNumberParameters(HttpServletRequest req) {
		try {
			List<Number> ids = new ArrayList<>();
			for (String token : req.getRequestURI().split("/")) {
				try {
					ids.add(Long.parseLong(StringUtils.trimToEmpty(token)));
				} catch (Exception notANumber) {}
			}
			return ids;
		} catch (Exception e) {
			return null;
		}
	}

	protected void write(HttpServletRequest request, HttpServletResponse response, Object output) {
		write(request, response, output, true);
	}

	protected void write(HttpServletRequest request, HttpServletResponse response, Object output, boolean success) {
		write(request, response, output, success, HttpServletResponse.SC_OK);
	}

	protected void write(HttpServletRequest request, HttpServletResponse response, Object output, boolean success, int status) {
		String serialised;
		MediaType responseType = getAcceptType(request);
		if (responseType.equals(MediaType.APPLICATION_XML_UTF_8)) {
			if (output != null) {
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(new Response(output, success, status).getClass());
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					StringWriter writer = new StringWriter();
					jaxbMarshaller.marshal(output, writer);
					serialised = writer.toString();
				} catch (Exception e) {
					internalError(request, response, e);
					return;
				}
			}
			else {
				serialised = null;
			}
		}
		else {
			Gson gson = JSON.gsonLowercaseUnderscores();
			if (this instanceof GsonProvider) {
				gson = ((GsonProvider) this).gson();
				if (gson == null) {
					gson = JSON.gsonLowercaseUnderscores();
				}
			}
			serialised = gson.toJson(new Response(output, success, status));
		}
		write(response, serialised, responseType, status);
	}

	protected void write(HttpServletResponse response, String output, int status) {
		write(response, output, MediaType.PLAIN_TEXT_UTF_8, status);
	}

	private void write(HttpServletResponse response, String output, MediaType contentType, int status) {
		try {
			response.setStatus(status);
			response.setContentType(contentType != null ? contentType.withoutParameters().toString() : MediaType.PLAIN_TEXT_UTF_8.withoutParameters().toString());
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.getWriter().println(output);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			try {
				if (!response.isCommitted()) {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println("An internal error occurred.");
					response.getWriter().flush();
					response.getWriter().close();
				}
			} catch (Exception e1) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}

	protected void writeHtml(HttpServletResponse resp, String html) {
		write(resp, html, MediaType.HTML_UTF_8, HttpServletResponse.SC_OK);
	}

	protected void redirect(HttpServletRequest request, HttpServletResponse response, String endpoint) {
		try {
			response.sendRedirect(endpoint);
		} catch (Exception e) {
			internalError(request, response, e);
		}
	}

	protected void internalError(HttpServletRequest request, HttpServletResponse response, Throwable exception) {
		String reason = "A 500 has been thrown on Hutt Parking:\n\n" + RequestDebugger.debug(request) + (exception != null ? "\n\nException follows:\n\n:" + ExceptionUtils.getStackTrace(exception) : "\n\nNo Exception trace.");
		writeError(request, response, reason, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	protected void unauthorised(HttpServletRequest request, HttpServletResponse response) {
		String reason = "You do not have permission to do that.";
		writeError(request, response, reason, HttpServletResponse.SC_FORBIDDEN);
	}

	protected void writeError(HttpServletRequest request, HttpServletResponse response, String reason, int code) {
		reason = StringUtils.trimToEmpty(reason).length() > 0 ? StringUtils.trimToEmpty(reason) : "Something went wrong, you may not have permission to do that.";
		write(request, response, new Response.Error(reason), false, code > 0 ? code : HttpServletResponse.SC_BAD_REQUEST);
	}
}