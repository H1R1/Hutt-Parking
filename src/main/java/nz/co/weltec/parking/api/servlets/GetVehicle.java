package nz.co.weltec.parking.api.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.co.weltec.parking.api.ApiServlet;
import nz.co.weltec.parking.database.DBConnection;

@WebServlet("/api/get-vehicle")
public class GetVehicle extends ApiServlet {

	private static final long serialVersionUID = 4729175515142157676L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String pnumber = request.getParameter("pnumber");
		String result = new DBConnection().getTime(Integer.parseInt(pnumber));
		if (result.length() > 0) {
			request.setAttribute("remaining", result);
		}
		else {
			redirect(request, response, "http://localhost:8080/home.jsp");
		}
	}

}