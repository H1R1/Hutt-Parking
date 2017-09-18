package nz.co.weltec.parking.utils;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

	private static final TimeZone TIME_ZONE 			= TimeZone.getTimeZone("Pacific/Auckland");
	private static final DateTimeZone DATE_TIME_ZONE = DateTimeZone.forTimeZone(TIME_ZONE);
	
	public static TimeZone getTimeZone() {
		return TIME_ZONE;
	}
	
	public static DateTimeZone getDateTimeZone() {
		return DATE_TIME_ZONE;
	}

	public static DateTime now() {
		return DateTime.now(DATE_TIME_ZONE);
	}

	public static DateTime from(DateTime date) {
		if (date != null) {
			return new DateTime(date.getMillis(), DATE_TIME_ZONE);
		}
		else {
			return null;
		}
	}

	public static DateTime from(Date date) {
		if (date != null) {
			return new DateTime(date, DATE_TIME_ZONE);
		}
		else {
			return null;
		}
	}

	public static DateTime from(Long date) {
		if (date != null) {
			return new DateTime(date.longValue(), DATE_TIME_ZONE);
		}
		else {
			return null;
		}
	}

	public static DateTimeFormatter formatter(String format) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
		formatter.withLocale(Locale.UK);
		formatter.withZone(DATE_TIME_ZONE);
		return formatter;
	}
	
	public static String format(Date date, String format) {
		return formatter(format).print(new DateTime(date));
	}
	
	public static String formatNow(String format) {
		return formatter(format).print(now());
	}
	
	public static DateTime parse(String date, String format) {
		return formatter(format).parseDateTime(date);
	}

}