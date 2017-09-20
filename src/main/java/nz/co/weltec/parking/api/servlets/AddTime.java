package nz.co.weltec.parking.api.servlets;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nz.co.weltec.parking.api.ApiServlet;
import nz.co.weltec.parking.database.DBConnection;

@WebServlet("/api/add-time")
public class AddTime extends ApiServlet {

	private static final long serialVersionUID = 4729175515142157676L;	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String parkNum = request.getParameter("park");
		String min = request.getParameter("minutes");
		try {
			if ((new DBConnection().addTime(Integer.parseInt(parkNum), Integer.parseInt(min))) > 0) {
				redirect(request, response, "http://localhost:8080/Home.jsp");
			}
			else {
				redirect(request, response, "http://localhost:8080/AddTime.jsp");
			}
		} catch (NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}