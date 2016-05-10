package com.dynatrace.diagnostics.btexport.flume;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.junit.Test;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;
import com.google.protobuf.InvalidProtocolBufferException;

public class BtExportHandlerTest {
	
	/**
	 * Tests if the Business Transactions included in a request are splitted correctly to seperate flume-events.
	 *
	 */
	@Test
	public void testBtSplittingPerType() throws Exception {
		BusinessTransactions.Builder builder = BusinessTransactions.newBuilder();
		for (BusinessTransaction.Type type : BusinessTransaction.Type.values()) {
			BusinessTransaction.Builder btBuilder = BusinessTransaction.newBuilder();
			btBuilder.setName("name_" + type);
			btBuilder.setType(type);
			builder.addBusinessTransactions(btBuilder);
		}
		
		BtExportHandler handler = new BtExportHandler();
		
		DummyRequest request = new DummyRequest(builder.build().toByteArray());
		
		List<Event> events = handler.getEvents(request);
		
		assertEquals(BusinessTransaction.Type.values().length, events.size());
		
		for (int i = 0; i < BusinessTransaction.Type.values().length; i++) {
			BusinessTransaction.Type type = BusinessTransaction.Type.values()[i];
			Event event = events.get(i);
			assertEquals(type.name(), event.getHeaders().get(BtExportHandler.HEADER_KEY_BT_TYPE));
			assertEquals("name_" + type, event.getHeaders().get(BtExportHandler.HEADER_KEY_BT_NAME));
			
			BusinessTransaction bt = BusinessTransaction.parseFrom(event.getBody());
			assertEquals(type, bt.getType());
			assertEquals("name_" + type, bt.getName());
		}
		
	}
	
	/**
	 * If a request with an unparsable protobuf message has been received a HTTPBadRequestException is expected to be thrown.
	 *
	 * @throws Exception
	 */
	@Test
	public void testInvalidProtobuf() throws Exception {
		BtExportHandler handler = new BtExportHandler();
		DummyRequest request = new DummyRequest(new byte[] {1, 2, 3});
		Exception exception = null; 
		try { 
			handler.getEvents(request);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertTrue(exception instanceof HTTPBadRequestException);
		assertTrue(exception.getCause() instanceof InvalidProtocolBufferException);
	}
	
}
