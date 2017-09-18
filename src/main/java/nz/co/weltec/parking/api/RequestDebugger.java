package nz.co.weltec.parking.api;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

import nz.co.weltec.parking.utils.DateUtil;

public class RequestDebugger {
	
	private RequestDebugger() {}

	public static String debug(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\nRequest for: " + request.getMethod() + " " + request.getRequestURI() + "\n\n");
		logRequestParameters(request, sb);
		logRequestHeaders(request, sb);
		logSessionAttributes(request, sb);
		logCookies(request, sb);
		return sb.toString();
	}

	private static void logRequestParameters(HttpServletRequest request, StringBuilder sb) {
		sb.append("<!-- REQUEST PARAMETERS -->\n");
		Map<String, String[]> sortedParams = sortRequestParameters(request);
		for (Map.Entry<String, String[]> entry : sortedParams.entrySet()){
			StringBuilder builder = new StringBuilder();
			for (String s : entry.getValue()) {
				builder.append(s);
				builder.append(", ");
			}
			sb.append(" " + entry.getKey() + ": " + builder.toString() + "\n");
		}
		sb.append("<!-- REQUEST PARAMETERS END -->\n\n");
	}

	private static void logRequestHeaders(HttpServletRequest request, StringBuilder sb) {
		sb.append("<!-- REQUEST HEADERS -->\n");
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = headers.nextElement();
			sb.append(header + ": " + request.getHeader(header) + "\n");
		}
		sb.append("<!-- REQUEST HEADERS END -->\n\n");
	}

	private static void logSessionAttributes(HttpServletRequest request, StringBuilder sb) {
		sb.append("<!-- SESSION " + request.getSession().getId() + " created at: " + DateUtil.format(new DateTime(request.getSession().getCreationTime()).toDate(), "yyyy-MM-dd HH:mm:ss:SSS") + "-->\n");
		sb.append("<!-- SESSION ATTRIBUTES -->\n");
		Map<String, Object> sortedAttrs = sortSessionAttributes(request);
		for (Map.Entry<String, Object> entry : sortedAttrs.entrySet()) {
			String description = "";
			try {
				description = BeanUtils.describe(entry.getValue()).toString();
			} catch (Exception e) {
				System.out.println("BeanUtils Exception describing attribute " + entry.getKey());
			}
			sb.append(" " + entry.getKey() + ": " + description + "\n");
		}
		sb.append("<!-- SESSION ATTRIBUTES END -->\n\n");
	}

	private static void logCookies(HttpServletRequest request, StringBuilder sb) {
		sb.append("<!-- COOKIES -->\n");
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				String description = "";
				try {
					description = BeanUtils.describe(cookie).toString();
				} catch (Exception e) {
					System.out.println("BeanUtils Exception describing cookie");
				}
				sb.append(" " + description + "\n");
			}
		}
		sb.append("<!-- COOKIES END -->\n");
	}

	private static Map<String, Object> sortSessionAttributes(HttpServletRequest request) {
		Map<String, Object> sortedAttrs = new TreeMap<String, Object>();
		Enumeration<String> attrEnum = request.getSession().getAttributeNames();
		while(attrEnum.hasMoreElements()){
			String s = attrEnum.nextElement();
			sortedAttrs.put(s, request.getAttribute(s));
		}
		return sortedAttrs;
	}

	private static Map<String, String[]> sortRequestParameters(HttpServletRequest request) {
		Map<String, String[]> sortedParams = new TreeMap<String, String[]>();
		Set<Map.Entry<String, String[]>> params = request.getParameterMap().entrySet();
		for(Map.Entry<String, String[]> entry : params){
			sortedParams.put(entry.getKey(), entry.getValue());
		}
		return sortedParams;
	}
}