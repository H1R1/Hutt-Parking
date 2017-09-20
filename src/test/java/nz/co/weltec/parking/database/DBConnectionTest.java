package nz.co.weltec.parking.database;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nz.co.weltec.parking.utils.DateUtil;

public class DBConnectionTest {

	DBConnection dbTest;
	String plate;
	String password;
	Number carpark;
	Number parkNum;
	Number minutes;
	
	@Before
	public void setUp() throws Exception {
		dbTest = new DBConnection();
		plate = "ABC123";
		password = "Testing123";
		carpark = 1234;
		parkNum = 1234;
		minutes = 30;
	}

	@After
	public void tearDown() throws Exception {
		dbTest = null;
		plate = null;
		password = null;
		carpark = null;
		parkNum = null;
		minutes = null;
	}

	@Test
	public void testSearchUserQuery() {
		String result = dbTest.searchUserQuery(plate, password);
		assertEquals(result, "SELECT * FROM account WHERE `licensePlate`='ABC123' and `password`='Testing123'");
	}

	@Test
	public void testCheckTicketQuery() {
		String result = dbTest.checkTicketQuery(plate);
		assertEquals(result, "SELECT * FROM account WHERE `licensePlate`='ABC123'");
	}

	@Test
	public void testGetTimeQuery() {
		String result = dbTest.getTimeQuery(carpark);
		assertSame(result, "SELECT endTime FROM carpark WHERE `idcarpark`='1234'");
	}

	@Test
	public void testCalcTime() {
		String result = dbTest.calcTime(parkNum, minutes);
		assertTrue(result.equals("UPDATE carpark SET `endTime`='" + (DateUtil.format(DateUtil.now().plusMinutes(minutes.intValue()).toDate(), "hh:mm")).toString() + "' WHERE `idcarpark` = '1234'"));
		//Must include DateUtil as the value changes due to current time. Unable to automatically test this, must be done manually
	}

}
