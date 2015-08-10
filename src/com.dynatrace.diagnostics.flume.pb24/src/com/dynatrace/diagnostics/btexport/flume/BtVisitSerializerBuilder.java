package com.dynatrace.diagnostics.btexport.flume;

import java.io.OutputStream;

import org.apache.flume.Context;
import org.apache.flume.serialization.EventSerializer;

public class BtVisitSerializerBuilder implements EventSerializer.Builder {

	/**
	 * Builds a {@link BtVisitSerializer}.
	 */
	@Override
	public EventSerializer build(Context context, OutputStream outputStream) {
		BtVisitSerializer serializer = new BtVisitSerializer(outputStream);
		serializer.configure(context);
		return serializer;
	}
	
}