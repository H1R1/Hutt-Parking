package nz.co.weltec.parking.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Helper for {@link BigDecimal}s
 */
class BigDecimalSerializer extends TypeAdapter<BigDecimal> {

	@Override
	public void write(JsonWriter out, BigDecimal value) throws IOException {
		if (value == null) {
			out.nullValue();
		}
		else {
			out.value(value);
		}
	}

	@Override
	public BigDecimal read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		String stringValue = in.nextString();
		try {
			return NumberUtils.createBigDecimal(stringValue);
		} catch (Exception e) {
			if (stringValue != null && stringValue.length() == 0) {
				return null;	
			}
			else {
				throw new IOException(e.getMessage());
			}
		}
	}
}