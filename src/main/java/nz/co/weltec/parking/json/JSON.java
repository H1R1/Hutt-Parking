package nz.co.weltec.parking.json;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utilities for mapping into and out of JSON
 * 
 */
public class JSON {

	/**
	 * Convenience to {@link JSON#stringify(Object, boolean)} with {@code false} pretty printing
	 * 
	 * @param obj 	Any type to JSON
	 * @return		Returns the JSON representation of the input
	 */
	public static String stringify(Object obj) {
		return stringify(obj, false);
	}

	/**
	 * Recreates the JS style JSON.stringify
	 * 
	 * @param obj	Any type to JSON
	 * @param pretty	{@code true} to pretty print
	 * 
	 * @return		Stringified version of {@link Object}, or null if not possible	
	 */
	public static String stringify(Object obj, boolean pretty) {
		if (obj != null) {
			try {
				GsonBuilder gsonBuilder = gsonBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
				if (pretty) {
					gsonBuilder.setPrettyPrinting();
				}
				return gsonBuilder.create().toJson(obj);
			} catch (Exception e) {
				System.out.print("Cannot JSON complex type " + obj.getClass().getSimpleName());
				return null;
			}
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the default barebones {@link Gson} for {@link Model}s on the system, sets
	 * {@link FieldNamingPolicy#IDENTITY}
	 *  
	 * @return Instance of {@link Gson}
	 */
	public static Gson gson() {
		return gsonBuilder(FieldNamingPolicy.IDENTITY).create();
	}

	/**
	 * Convenience method for {@link #defaultGsonBuilder(FieldNamingPolicy)} with
	 * {@link FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES}
	 * 
	 * @return {@link Gson} instance with {@link FieldNamingPolicy#LOWER_CASE_WITH_UNDERSCORES}
	 */
	public static Gson gsonLowercaseUnderscores() {
		return gsonBuilder(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
	}

	/**
	 * Returns the default barebones {@link GsonBuilder} for {@link Model}s on the system
	 *  
	 * @param fieldNaming 	Required convention that field names are in in the JSON payload
	 * @return 				Instance of {@link GsonBuilder}
	 */
	public static GsonBuilder gsonBuilder(FieldNamingPolicy fieldNaming) {
		return gsonBuilderExcludingSerializers(fieldNaming, (Type[]) null);
	}

	static GsonBuilder gsonBuilderExcludingSerializers(FieldNamingPolicy fieldNaming, Type... excludedTypeSerializers) {
		List<Type> excludedSerializersList = Arrays.asList(excludedTypeSerializers != null ? excludedTypeSerializers : new Type[0]);
		GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(fieldNaming);
		if (!excludedSerializersList.contains(BigDecimal.class)) {
			builder.registerTypeAdapter(BigDecimal.class, new BigDecimalSerializer());
		}
		if (!excludedSerializersList.contains(java.util.Date.class)) {
			builder.registerTypeAdapter(java.util.Date.class, new DateSerializer<java.util.Date>());
		}
		if (!excludedSerializersList.contains(java.sql.Date.class)) {
			builder.registerTypeAdapter(java.sql.Date.class, new DateSerializer<java.sql.Date>());
		}
		if (!excludedSerializersList.contains(Timestamp.class)) {
			builder.registerTypeAdapter(Timestamp.class, new DateSerializer<Timestamp>());
		}
		if (!excludedSerializersList.contains(Boolean.class)) {
			builder.registerTypeAdapter(Boolean.class, new BooleanSerializer());
		}
		if (!excludedSerializersList.contains(boolean.class)) {
			builder.registerTypeAdapter(boolean.class, new BooleanSerializer());
		}
		return builder;
	}
}
