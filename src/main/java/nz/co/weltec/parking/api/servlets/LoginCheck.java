package nz.co.weltec.parking.api.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.co.weltec.parking.api.ApiServlet;
import nz.co.weltec.parking.database.DBConnection;

@WebServlet("/api/login-check")
public class LoginCheck extends ApiServlet {

	private static final long serialVersionUID = 4729175515142157676L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String uname = request.getParameter("uname");
		String password = request.getParameter("password");
		if (new DBConnection().searchUser(uname, password)) {
			redirect(request, response, "home.jsp");
		}
		else {
			redirect(request, response, "AddTime.jsp");
		}
	}

}