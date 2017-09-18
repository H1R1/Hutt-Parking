package nz.co.weltec.parking.database;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class ParkingTime {
	static int timeToPark = 0;
	
	public static String startTime() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		cal.setTime(sdf.getCalendar().getTime());
		return null;
	}
	
	public static String endTime(int minutesToPark) {
		timeToPark = minutesToPark;
		LocalTime lt = LocalTime.parse(startTime());
		LocalTime ltLate = lt.plusMinutes(minutesToPark);
		String endTime = ltLate.toString();
		return endTime;
	}

}
