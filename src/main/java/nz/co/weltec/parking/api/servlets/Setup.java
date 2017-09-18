package nz.co.weltec.parking.api.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.co.weltec.parking.api.ApiServlet;

@WebServlet("/api/setup")
public class Setup extends ApiServlet {

	private static final long serialVersionUID = 7553106706159032286L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		try {
			write(request, response, "Served at: " + request.getContextPath());
		} catch (Exception e) {
			internalError(request, response, e);
		}
	}

}
