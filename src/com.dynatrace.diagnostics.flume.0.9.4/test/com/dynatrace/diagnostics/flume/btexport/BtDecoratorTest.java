package com.dynatrace.diagnostics.flume.btexport;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;

import org.junit.Test;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction.Type;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;

public class BtDecoratorTest {
	
	static final String ESCAPED_FIELD_DELIMITER = new StringBuilder(2).append(BtDecorator.ESCAPE_CHAR).append(BtDecorator.FIELD_DELIMITER).toString();
	static final String ESCAPED_COLLECTION_DELIMITER = new StringBuilder(2).append(BtDecorator.ESCAPE_CHAR).append(BtDecorator.COLLECTION_DELIMITER).toString();
	static final String ESCAPED_MAP_KEY_DELIMITER = new StringBuilder(2).append(BtDecorator.ESCAPE_CHAR).append(BtDecorator.MAP_KEY_DELIMITER).toString();
	static final String ESCAPED_LINE_DELIMITER = new StringBuilder(2).append(BtDecorator.ESCAPE_CHAR).append(BtDecorator.LINE_DELIMITER_ESCAPED).toString();
	
	
	class TestBtDecorator extends BtDecorator<EventSink> {
		
		public TestBtDecorator() {
			super(null, false, null);
		}
		
		public TestBtDecorator(boolean numericDate) {
			super(null, numericDate, null);
		}
		
		
		public TestBtDecorator(EventSink sink, String charsetName) {
			super(sink, false, charsetName);
		}

		
		@Override
		void appendOccurrence(StringBuilder sb, BusinessTransaction bt,	BtOccurrence occurrence) {
		}

		
		@Override
		Type getBtType() {
			return null;
		}
	};
	
	
	class TestEncodingBtDecorator extends TestBtDecorator {
		
		
		public TestEncodingBtDecorator(EventSink sink, String charsetName) {
			super(sink, charsetName);
		}
		
		@Override
		String btToString(BusinessTransaction bt) {
			return "aü";
		}
		
	}
	
	
	/**
	 * verifies that the different delimiters for the Hive tables are escaped correctly.
	 */
	@Test
	public void testEscape() {
		BtDecorator<?> decorator = new TestBtDecorator();
		assertEquals(ESCAPED_FIELD_DELIMITER, String.valueOf(decorator.escape(String.valueOf(BtDecorator.FIELD_DELIMITER))));
		assertEquals(ESCAPED_COLLECTION_DELIMITER, String.valueOf(decorator.escape(String.valueOf(BtDecorator.COLLECTION_DELIMITER))));
		assertEquals(ESCAPED_MAP_KEY_DELIMITER, String.valueOf(decorator.escape(String.valueOf(BtDecorator.MAP_KEY_DELIMITER))));
		assertEquals(ESCAPED_LINE_DELIMITER, String.valueOf(decorator.escape(String.valueOf(BtDecorator.LINE_DELIMITER))));
	}
	
	
	/**
	 * verifies that a {@link String} not containing a delimter isn't modified.
	 */
	@Test
	public void testEscapNothing() {
		BtDecorator<?> decorator = new TestBtDecorator();
		assertEquals("nothing", String.valueOf(decorator.escape("nothing")));
	}
	
	
	/**
	 * verifies that setting serializer-encoding to ASCII works as expected
	 *
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Test
	public void testAsciiEncoding() throws Exception {
		DummySink sink = new DummySink();
		TestBtDecorator decorator = new TestEncodingBtDecorator(sink, "ASCII");
		decorator.open();
		decorator.append(createSimpleEvent());
		assertArrayEquals(new byte[] {97, 63}, sink.lastEvent.getBody());
	}
	
	
	/**
	 * verifies that setting serializer-encoding to UTF-8 works as expected
	 *
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Test
	public void testUTF8Encoding() throws Exception {
		DummySink sink = new DummySink();
		TestBtDecorator decorator = new TestEncodingBtDecorator(sink, "UTF-8");
		decorator.open();
		decorator.append(createSimpleEvent());
		assertArrayEquals(new byte[] {97, -61, -68}, sink.lastEvent.getBody());
	}

	
	/**
	 * verifies that setting serializer-encoding to a non existing charset turns off serialization 
	 *
	 * @throws IOException
	 */
	@Test
	public void testWrongEncoding() throws Exception {
		boolean exceptionThrown = false;
		try {
			new TestEncodingBtDecorator(null, "nonexistingcharset");
		} catch (UnsupportedCharsetException uce) {
			exceptionThrown = true;
		}
		assertTrue("Expected an UnsupportedCharsetException", exceptionThrown);
		
	}
	
	
	private Event createSimpleEvent() {
		BusinessTransactions.Builder bts = BusinessTransactions.newBuilder();
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setType(Type.PUREPATH);
		bt.setName("bt");
		bts.addBusinessTransactions(bt);
		return new DummyEvent(bts.build().toByteArray());
	}
	
	
	@Test
	public void testAppendDateAsNumber() {
		BtDecorator<?> decorator = new TestBtDecorator(true);
		StringBuilder sb = new StringBuilder();
		decorator.appendDate(sb, 12345678900L);
		assertEquals("12345678.900", sb.toString());
	}
	
	
	@Test
	public void testAppendDateAsString() {
		BtDecorator<?> decorator = new TestBtDecorator(false);
		StringBuilder sb = new StringBuilder();
		decorator.appendDate(sb, 123456789000L);
		assertEquals("1973-11-29 22:33:09.0", sb.toString());
	}

	
}
