package com.dynatrace.diagnostics.btexport.flume;

import java.io.OutputStream;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.EventSerializer;

public class BtPurePathSerializerBuilder implements EventSerializer.Builder {

	/**
	 * Builds a {@link BtPurePathSerializer} which will serialize Flume {@link Event}s to the given {@link OutputStream}.
	 * One line per {@link Event} will be written. Special Characters are escaped to guarantee the serialized data can be
	 * read correctly by Hive.
	 * 
	 * @param outputStream the {@link OutputStream} the {@link Event}s will be serialized to.
	 */
	@Override
	public EventSerializer build(Context context, OutputStream outputStream) {
		BtPurePathSerializer serializer =  new BtPurePathSerializer(outputStream);
		serializer.configure(context);
		return serializer;
	}
	
}