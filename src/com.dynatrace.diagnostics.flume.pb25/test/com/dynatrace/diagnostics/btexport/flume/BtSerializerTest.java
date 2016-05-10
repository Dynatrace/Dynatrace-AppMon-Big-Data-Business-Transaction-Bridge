package com.dynatrace.diagnostics.btexport.flume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.junit.Test;

public class BtSerializerTest {
	
	static final String ESCAPED_FIELD_DELIMITER = new StringBuilder(2).append(BtSerializer.ESCAPE_CHAR).append(BtSerializer.DEFAULT_FIELD_DELIMITER).toString();
	static final String ESCAPED_COLLECTION_DELIMITER = new StringBuilder(2).append(BtSerializer.ESCAPE_CHAR).append(BtSerializer.DEFAULT_COLLECTION_DELIMITER).toString();
	static final String ESCAPED_MAP_KEY_DELIMITER = new StringBuilder(2).append(BtSerializer.ESCAPE_CHAR).append(BtSerializer.MAP_KEY_DELIMITER).toString();
	static final String ESCAPED_LINE_DELIMITER = new StringBuilder(2).append(BtSerializer.ESCAPE_CHAR).append(BtSerializer.LINE_DELIMITER_ESCAPED).toString();
	
	
	/**
	 * helper class 
	 *
	 */
	static class TestBtSerializer extends BtSerializer {
		
		private ByteArrayOutputStream outputStream;
		
		private TestBtSerializer(ByteArrayOutputStream outputStream) {
			super(outputStream);
			this.outputStream = outputStream;
		}
		
		static TestBtSerializer newInstance() {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			return new TestBtSerializer(outputStream);
		}

		@Override
		public void write(String string) throws IOException {
			super.write(string);
			super.flush();
		}

		@Override
		public void write(Event arg0) throws IOException {
			// do nothing
		}
		
		byte[] getData() {
			return outputStream.toByteArray();
		}
	};
	
	
	/**
	 * verifies that the different delimiters for the Hive tables are escaped correctly.
	 */
	@Test
	public void testEscape() {
		BtSerializer serializer = TestBtSerializer.newInstance();
		assertEquals(ESCAPED_FIELD_DELIMITER, String.valueOf(serializer.escape(String.valueOf(BtSerializer.DEFAULT_FIELD_DELIMITER))));
		assertEquals(ESCAPED_COLLECTION_DELIMITER, String.valueOf(serializer.escape(String.valueOf(BtSerializer.DEFAULT_COLLECTION_DELIMITER))));
		assertEquals(ESCAPED_MAP_KEY_DELIMITER, String.valueOf(serializer.escape(String.valueOf(BtSerializer.MAP_KEY_DELIMITER))));
		assertEquals(ESCAPED_LINE_DELIMITER, String.valueOf(serializer.escape(String.valueOf(BtSerializer.LINE_DELIMITER))));
	}
	
	
	/**
	 * verifies that a {@link String} not containing a delimter isn't modified.
	 */
	@Test
	public void testEscapeNothing() {
		BtSerializer serializer = TestBtSerializer.newInstance();
		assertEquals("nothing", String.valueOf(serializer.escape("nothing")));
	}
	
	
	/**
	 * verifies that setting serializer-encoding to ASCII works as expected
	 *
	 * @throws IOException
	 */
	@Test
	public void testAsciiEncoding() throws IOException {
		TestBtSerializer serializer = TestBtSerializer.newInstance();
		configureEncoding(serializer, "ASCII");
		serializer.write("a�");
		assertArrayEquals(new byte[] {97, 63, 63, 63}, serializer.getData());
	}
	
	
	/**
	 * verifies that setting serializer-encoding to UTF-8 works as expected
	 *
	 * @throws IOException
	 */
	@Test
	public void testUTF8Encoding() throws IOException {
		TestBtSerializer serializer = TestBtSerializer.newInstance();
		configureEncoding(serializer, "UTF-8");
		serializer.write("a�");
		assertArrayEquals(new byte[] {97, -61, -81, -62, -65, -62, -67}, serializer.getData());
	}

	
	/**
	 * verifies that setting serializer-encoding to a non existing charset turns off serialization 
	 *
	 * @throws IOException
	 */
	@Test
	public void testWrongEncoding() throws IOException {
		TestBtSerializer serializer = TestBtSerializer.newInstance();
		configureEncoding(serializer, "nonexistingcharset");
		serializer.write("a�");
		assertArrayEquals(new byte[] {}, serializer.getData());
	}
	
	
	private void configureEncoding(BtSerializer serializer, String charset) {
		Context context = new Context();
		context.put(BtSerializer.CONFIG_CHARSET, charset);
		serializer.configure(context);
	}
	
	
	/**
	 * verify that appending the date as a number works correctly.
	 */
	@Test
	public void testAppendDateAsNumber() {
		TestBtSerializer serializer = TestBtSerializer.newInstance();
		StringBuilder sb = new StringBuilder();
		Context context = new Context();
		context.put(BtSerializer.CONFIG_DATE_NUMERIC, "true");
		serializer.configure(context); 
		serializer.appendDate(sb, 12345678900L);
		assertEquals("12345678.900", sb.toString());
	}
	
	
	/**
	 * verify that appending the date as a number works correctly.
	 */
	@Test
	public void testAppendDateAsString() {
		TestBtSerializer serializer = TestBtSerializer.newInstance();
		StringBuilder sb = new StringBuilder();
		serializer.appendDate(sb, 123456789000L);
		assertEquals("1973-11-29 22:33:09.0", sb.toString());
	}
	
	
}
