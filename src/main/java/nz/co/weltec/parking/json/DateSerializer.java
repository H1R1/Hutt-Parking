package nz.co.weltec.parking.json;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import nz.co.weltec.parking.utils.DateUtil;

/**
 * Helper for {@link Date}s
 */
class DateSerializer<T extends Date> implements JsonDeserializer<T>, JsonSerializer<T> {

	/**
	 * Helper to parse a {@link String} in the .NET date format
	 * to a {@link Date}
	 * 
	 */
	private static final class DotNetJSONDate {

		/**
		 * Returns the {@link DateTime} from a .NET formatted {@link String}
		 * 
		 * @param date	The {@link DateTime} to format (as a {@String})
		 * @return		A {@link DateTime}
		 */
		public static DateTime stringToDateTime(String date) {
			if (date == null || !date.contains("Date") || date.contains("Date(0)")) {
				return null;
			}
			String numbersOnly = "";
			for (int i = 0; i < date.length(); i++) {
				if (Character.isDigit(date.charAt(i))) {
					numbersOnly += date.charAt(i);
				}
				if (date.charAt(i) == '+' || date.charAt(i) == '-') {
					break;
				}
			}
			Long possibleUnixTimestamp = null;
			if (numbersOnly.length() >= 10) {
				possibleUnixTimestamp = Long.parseLong(numbersOnly);
			}
			else {
				possibleUnixTimestamp = Long.parseLong(numbersOnly);
				possibleUnixTimestamp /= 1000;
			}
			DateTime dateTime = DateUtil.from(possibleUnixTimestamp);
			return dateTime;
		}
	}

	/* Some constants */
	public static Boolean noisyDebug										= false;
	private static final String JAVA_DATE_FORMAT 						= "MMM dd, yyyy HH:mm:ss a";
	private static final String MYSQL_DATE_FORMAT 						= "yyyy-MM-dd";
	private static final String MYSQL_DATETIME_FORMAT 					= "yyyy-MM-dd HH:mm:ss";
	private static final String MYSQL_TIMESTAMP_FORMAT 					= "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final String MYSQL_TIMESTAMP_MILLISECONDS_FORMAT 		= "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String MYSQL_TIMESTAMP_FORMAT_ALT 				= "yyyy-MM-dd'T'HH:mm:ss Z";
	private static final String MYSQL_TIMESTAMP_MILLISECONDS_FORMAT_ALT 	= "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String MYSQL_YEAR_FORMAT 						= "yyyy";
	private static final String MYSQL_YEAR_FORMAT_ALT						= "yy";
	private static final String JAVA_SIMPLE_DATE_FORMAT					= "MMM d, yy";
	private static final String JAVA_SIMPLE_DATE_FORMAT_ALT 				= "d/MM/yyyy";

	@Override
	@SuppressWarnings("unchecked")
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (json == null || json.getAsJsonPrimitive() == null || json.getAsJsonPrimitive().getAsString() == null || json.getAsJsonPrimitive().getAsString().length() == 0) {
			return null;
		}
		else {
			String dateString = json.getAsJsonPrimitive().getAsString();
			DateTime dateTime = null;
			if (dateString.endsWith("Z")) {
				dateString = dateString.replace("Z", "+0000");
			}
			if (dateTime == null) {
				try {
					dateTime = DotNetJSONDate.stringToDateTime(dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using .NET time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using .NET time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(JAVA_DATE_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using Java time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using Java time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_DATETIME_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL datetime time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL datetime time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_TIMESTAMP_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL timestamp time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL timestamp time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_TIMESTAMP_MILLISECONDS_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL timestamp (ms) time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL timestamp (ms) time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_TIMESTAMP_FORMAT_ALT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL datetime alt to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL timestamp alt time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_TIMESTAMP_MILLISECONDS_FORMAT_ALT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL datetime (ms) alt to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL timestamp (ms) alt time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_DATE_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL date time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL date time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_YEAR_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL year-only time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL year-only time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(MYSQL_YEAR_FORMAT_ALT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using MySQL year-only alt time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using MySQL year-only alt time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(JAVA_SIMPLE_DATE_FORMAT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using Java simple date time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using Java simple date time");
					}
				}
			}
			if (dateTime == null) {
				try {
					dateTime = parse(JAVA_SIMPLE_DATE_FORMAT_ALT, dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Converted \"" + dateString + "\" using Java simple date alt time to " + dateTime);
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to convert using Java simple date alt time");
					}
				}
			}
			if (dateTime == null) {
				Long possibleUnixTimestamp = null;
				try {
					possibleUnixTimestamp = Long.parseLong(dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Managed to parse out something looking like a unix timestamp: " + possibleUnixTimestamp + "...");
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to look for a unix timestamp");
					}
				}
				if (possibleUnixTimestamp != null && dateString.length() == 10) {
					try {
						dateTime = DateUtil.from(possibleUnixTimestamp * Long.valueOf(1000));
						if (noisyDebug) {
							System.out.println("[INFO]: Converted \"" + dateString + "\" using unix time to " + dateTime);
						}
					} catch (Exception e) {
						if (noisyDebug) {
							System.out.println("[INFO]: Exception thrown attempting to convert using unix time");
						}
					}
				}
				else if (noisyDebug) {
					System.out.println("[INFO]: Could not use the timestamp");
				}
			}
			if (dateTime == null) {
				Long possibleMillisecondTimestamp = null;
				try {
					possibleMillisecondTimestamp = Long.parseLong(dateString);
					if (noisyDebug) {
						System.out.println("[INFO]: Managed to parse out something looking like a unix timestamp (milliseconds): " + possibleMillisecondTimestamp + "...");
					}
				} catch (Exception e) {
					if (noisyDebug) {
						System.out.println("[INFO]: Exception thrown attempting to look for a unix timestamp (milliseconds)");
					}
				}
				if (possibleMillisecondTimestamp != null && dateString.length() == 12) {
					try {
						dateTime = DateUtil.from(possibleMillisecondTimestamp * Long.valueOf(1000));
						if (noisyDebug) {
							System.out.println("[INFO]: Converted \"" + dateString + "\" using unix time (milliseconds) to " + dateTime);
						}
					} catch (Exception e) {
						if (noisyDebug) {
							System.out.println("[INFO]: Exception thrown attempting to convert using unix time (milliseconds)");
						}
					}
				}
				else if (noisyDebug) {
					System.out.println("[INFO]: Could not use the timestamp (milliseconds)");
				}
			}
			if (dateTime != null) {
				if (typeOfT instanceof Timestamp) {
					return (T) new Timestamp(dateTime.getMillis());
				}
				else if (typeOfT instanceof java.sql.Date) {
					return (T) new java.sql.Date(dateTime.getMillis());
				}
				else {
					return (T) new java.util.Date(dateTime.getMillis());
				}
			}
			else {
				return null;
			}
		}
	}

	/**
	 * Does the actual parsing
	 * 
	 * @param format		Date format to parse as
	 * @param date			The item to parse
	 * 
	 * @return				A formatted {@link DateTime}
	 * 
	 * @throws Exception 	Exceptions passed up the chain
	 */
	public static DateTime parse(String format, String date) throws Exception {
		return DateTimeFormat
				.forPattern(format)
				.withLocale(Locale.UK)
				.withZone(DateUtil.getDateTimeZone())
				.parseDateTime(date)
				.toDateTime();
	}

	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(DateUtil.format(src, MYSQL_TIMESTAMP_FORMAT));
	} 
}