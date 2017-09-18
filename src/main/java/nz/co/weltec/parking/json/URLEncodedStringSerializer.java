package nz.co.weltec.parking.json;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.google.common.base.Charsets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Helper for {@link String}s
 */
class URLEncodedStringSerializer extends TypeAdapter<String> {

	@Override 
	public void write(JsonWriter out, String value) throws IOException {
		if (value == null) {
			out.nullValue();
		} else {
			out.value(URLEncoder.encode(value, Charsets.UTF_8.name()));
		}
	}

	@Override 
	public String read(JsonReader in) throws IOException {
		JsonToken peek = in.peek();
		switch (peek) {
		case BOOLEAN:
			return String.valueOf(in.nextBoolean());
		case NULL:
			in.nextNull();
			return null;
		case NUMBER:
			return String.valueOf(in.nextDouble());
		case STRING:
			String next = in.nextString();
			try {
				return URLDecoder.decode(String.valueOf(next), Charsets.UTF_8.name());
			} catch (Exception e) {
				return next;
			}
		default:
			throw new IllegalStateException("Expected STRING but was " + peek);
		}
	}
}