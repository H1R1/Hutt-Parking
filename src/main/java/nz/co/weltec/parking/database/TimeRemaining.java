package nz.co.weltec.parking.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class TimeRemaining {
	
	public int calcDiff(String endTime) throws ParseException {
		Calendar cal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		endCal.setTime(sdf.parse(endTime));
		int hour = endCal.get(Calendar.HOUR);
		int minute = endCal.get(Calendar.MINUTE);
		minute += hour*60;
		int hour2 = cal.get(Calendar.HOUR);
		int minute2 = cal.get(Calendar.MINUTE);
		minute2 += hour2*60;
		int totalMin = minute2 - minute;
		return totalMin;
	}
	
	public static String timeDiff(int totalTime) {
		int time = totalTime;
		if(time > 0) {
			double  conTime = time / 60;
			int hour = (int) conTime;
			double frac = conTime - hour;
			double min = frac * 60;
			String stuff = Integer.toString(hour) + ":" + Double.toString(min);
			return stuff;
		}
		else
		{
			return null;
		}
	}
}
