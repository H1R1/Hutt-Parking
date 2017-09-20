package nz.co.weltec.parking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import com.ocpsoft.pretty.time.PrettyTime;

import nz.co.weltec.parking.utils.DateUtil;

public class DBConnection {

	private static Connection CONNECTION;

	public DBConnection() {
		if (CONNECTION == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost:3306/huttparking?useSSL=false", "root", "Testing123");
				System.out.println("Initialising database...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int searchUser(@Nonnull String plate, @Nonnull String password) {
		PreparedStatement preparedStatement = null;
		int resultSet = 0;
		try {
		/*	preparedStatement = CONNECTION.prepareStatement("SELECT * FROM account WHERE `licensePlate`=? and `password`=?");
			preparedStatement.setString(1, plate);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			else {
				return false;
			}*/
			preparedStatement = CONNECTION.prepareStatement(searchUserQuery(plate, password));
			resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { preparedStatement.close(); } catch (Exception e) {}
		}
		return 0;
	}

	public String searchUserQuery(String plate, @Nonnull String password) {
		
		String statement = ("SELECT * FROM account WHERE `licensePlate`='" + plate + "' and `password`='" + password +  "'");
		return statement;
	}
	
	public int checkTicketed(@Nonnull String plate) {
		PreparedStatement preparedStatement = null;
		int resultSet = 0;
		try {
		/*	preparedStatement = CONNECTION.prepareStatement("SELECT * FROM account WHERE `licensePlate`=?");
			preparedStatement.setString(1, plate);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			else {
				return false;
			}*/
			preparedStatement = CONNECTION.prepareStatement(checkTicketQuery(plate));
			resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { preparedStatement.close(); } catch (Exception e) {}
		}
		return 0;
	}
	
	public String checkTicketQuery(String plate) {
		
		String statement = ("SELECT * FROM account WHERE `licensePlate`='" + plate + "'");
		return statement;
	}

	public int getTime(@Nonnull Number carpark) {
		PreparedStatement preparedStatement = null;
		int resultSet = 0;
		try {
			/*preparedStatement = CONNECTION.prepareStatement("SELECT endTime FROM carpark WHERE `idcarpark`=?");
			preparedStatement.setInt(1, carpark.intValue());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getString(1);
			}
			else {
				return null;
			}*/
			preparedStatement = CONNECTION.prepareStatement(getTimeQuery(carpark));
			resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				try { preparedStatement.close(); } catch (Exception e) {}
		}		
		return 0;
	}
	
	public String getTimeQuery(Number carpark) {
		
		String statement = ("SELECT endTime FROM carpark WHERE `idcarpark`=" + carpark.intValue());
		return statement;
	}
	

	public int addTime(@Nonnull Number parkNum, @Nonnull Number minutes) throws SQLException {
		PreparedStatement preparedStatement = null;
		int resultSet = 0;
//		try {
//			preparedStatement = CONNECTION.prepareStatement("UPDATE carpark SET `endTime`=? WHERE `idcarpark`=?");
//			preparedStatement.setString(1, DateUtil.format(DateUtil.now().plusMinutes(minutes.intValue()).toDate(), "hh:mm"));
//			preparedStatement.setInt(2, parkNum.intValue());
//			resultSet = preparedStatement.executeUpdate();
//			return resultSet;
//		}
		try {
			preparedStatement = CONNECTION.prepareStatement(calcTime(parkNum, minutes));
			resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { preparedStatement.close(); } catch (Exception e) {}
		}		
		return 0;
	}
	
	public String calcTime(Number parkNum, Number minutes) {
		String statement = ("UPDATE carpark SET `endTime`='" + (DateUtil.format(DateUtil.now().plusMinutes(minutes.intValue()).toDate(), "hh:mm")).toString() + "' WHERE `idcarpark` = " + parkNum.intValue());
		return statement;
	}
}
