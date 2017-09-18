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
				CONNECTION = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_login", "root", "");
				System.out.println("Initialising database...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean searchUser(@Nonnull String plate, @Nonnull String password) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = CONNECTION.prepareStatement("SELECT * FROM account WHERE `licensePlate`=? and `password`=?");
			preparedStatement.setString(1, plate);
			preparedStatement.setString(2, password);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			else {
				return false;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { preparedStatement.close(); } catch (Exception e) {}
		}
		return false;
	}

	public boolean checkTicketed(@Nonnull String plate) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = CONNECTION.prepareStatement("SELECT * FROM account WHERE `licensePlate`=?");
			preparedStatement.setString(1, plate);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
			else {
				return false;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { preparedStatement.close(); } catch (Exception e) {}
		}
		return false;
	}

	public String getTime(@Nonnull Number carpark) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = CONNECTION.prepareStatement("SELECT endTime FROM carpark WHERE `idcarpark`=?");
			preparedStatement.setInt(1, carpark.intValue());
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return new PrettyTime().format(DateUtil.parse(resultSet.getString(0), "hh:mm").toDate());
			}
			else {
				return null;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { preparedStatement.close(); } catch (Exception e) {}
		}		
		return null;
	}

	public boolean addTime(@Nonnull Number parkNum, @Nonnull Number minutes) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = CONNECTION.prepareStatement("UPDATE carpark SET `endTime`=? WHERE `idcarpark`=?");
			preparedStatement.setString(1, DateUtil.format(DateUtil.now().plusMinutes(minutes.intValue()).toDate(), "hh:mm"));
			preparedStatement.setInt(2, parkNum.intValue());
			resultSet = preparedStatement.executeQuery();
			return resultSet.next();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { resultSet.close(); } catch (Exception e) {}
			try { preparedStatement.close(); } catch (Exception e) {}
		}		
		return false;
	}
}
