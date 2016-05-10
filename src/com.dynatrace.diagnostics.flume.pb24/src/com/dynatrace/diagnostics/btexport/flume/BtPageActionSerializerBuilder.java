package com.dynatrace.diagnostics.btexport.flume;

import java.io.OutputStream;

import org.apache.flume.Context;
import org.apache.flume.serialization.EventSerializer;

public class BtPageActionSerializerBuilder implements EventSerializer.Builder {

	/**
	 * Builds a {@link BtPageActionSerializer}.
	 */
	@Override
	public EventSerializer build(Context context, OutputStream outputStream) {
		BtPageActionSerializer serializer =  new BtPageActionSerializer(outputStream);
		serializer.configure(context);
		return serializer;
	}
	
}