package com.dynatrace.diagnostics.btexport.flume;

import java.io.OutputStream;

import org.apache.flume.Context;
import org.apache.flume.serialization.EventSerializer;


public class BtExportJSONSerializerBuilder  implements EventSerializer.Builder {

	/**
	 * Builds a {@link BtVisitSerializer}.
	 */
	@Override
	public EventSerializer build(Context context, OutputStream outputStream) {
		BtExportJSONSerializer serializer = new BtExportJSONSerializer(outputStream);
		serializer.configure(context);
		return serializer;
	}
	
}