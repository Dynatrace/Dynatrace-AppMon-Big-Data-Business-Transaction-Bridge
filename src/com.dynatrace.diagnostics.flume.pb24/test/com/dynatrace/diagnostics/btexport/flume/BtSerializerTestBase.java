package com.dynatrace.diagnostics.btexport.flume;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.serialization.EventSerializer;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public abstract class BtSerializerTestBase {

	/**
	 * Helper method to simplify the test code.
	 * Performs the steps of building the protobuf message and serializing it to a {@link String} using the {@link EventSerializer} to test.
	 *
	 * @param bt
	 * @param occurrence
	 * @return
	 * @throws IOException
	 */
	protected String buildAndSerializeToString(BusinessTransaction.Builder bt, BtOccurrence.Builder occurrence, String server) throws IOException {
		return new String(buildAndSerialize(bt, occurrence, null, server));
	}


	/**
	 * Helper method to simplify the test code.
	 * Performs the steps of building the protobuf message and serializing it to a {@link String} using the {@link EventSerializer} to test.
	 *
	 * @param bt
	 * @param occurrence
	 * @return
	 * @throws IOException
	 */
	protected byte[] buildAndSerialize(BusinessTransaction.Builder bt, BtOccurrence.Builder occurrence, String charsetName, String server) throws IOException {
		bt.addOccurrences(occurrence);
		
		Event event = EventBuilder.withBody(bt.build().toByteArray());
		if (server != null) {
			event.getHeaders().put(BtExportHandler.HEADER_KEY_SERVER, server);
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		context.put(BtSerializer.CONFIG_CHARSET, charsetName);
		
		EventSerializer serializer = getSerializerBuilder().build(context, baos);
		serializer.write(event);
		serializer.flush();
		return baos.toByteArray();
	}

	
	/**
	 * 
	 * @return an {@link EventSerializer.Builder} to build the {@link EventSerializer} to be tested. 
	 */
	abstract protected EventSerializer.Builder getSerializerBuilder();
	
}
