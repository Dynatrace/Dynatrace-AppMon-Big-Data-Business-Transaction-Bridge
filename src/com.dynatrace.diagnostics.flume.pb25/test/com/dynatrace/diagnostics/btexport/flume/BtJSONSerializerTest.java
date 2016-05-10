package com.dynatrace.diagnostics.btexport.flume;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;
import org.junit.Test;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.google.gson.JsonObject;

public class BtJSONSerializerTest{

	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set and multiple measures is serialized correctly. 
	 *
	 */
	@Test
	public void testFullSerialization2ElemMaps() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.VISIT);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey1", "splittingKey2"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey1", "measureKey2"}));
		bt.setSystemProfile("sp");
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting1", "splitting2"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0, 2.0}));
		occurrence.addConvertedBy("converted");
		occurrence.addConvertedBy("by");
		
		occurrence.setFailed(false).setVisitId(1234)
				.setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
		String json = buildAndSerialize(bt, occurrence, "srv");
		
		assertEquals("{\"name\":\"btName\",\"application\":\"btApplication\",\"systemProfile\":\"sp\",\"server\":\"srv\",\"type\":\"VISIT\",\"purePathId\":\"PT\\u003d288;PA\\u003d-508867027;PS\\u003d-522660323\",\"startTime\":\"2013-01-16 11:05:57.840+0100\",\"dimensions\":{\"splittingKey1\":\"splitting1\",\"splittingKey2\":\"splitting2\"},\"measures\":{\"measureKey1\":1.0,\"measureKey2\":2.0},\"failed\":false,\"visitId\":1234,\"responseTime\":10.0,\"duration\":9.0,\"cpuTime\":8.0,\"execTime\":7.0,\"suspensionTime\":6.0,\"syncTime\":5.0,\"waitTime\":4.0,\"convertedBy\":[\"converted\",\"by\"]}\n", json);
		
	}


	
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with the minimum required values set is serialized correctly. 
	 *
	 */
	@Test
	public void testMinimalSerialization() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.PUREPATH);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		
		String json = buildAndSerialize(bt, occurrence, null);
		
		assertEquals("{\"name\":\"btName\",\"type\":\"PUREPATH\",\"startTime\":\"2013-01-16 11:05:57.840+0100\"}\n", json);
	}
	
	
	private String buildAndSerialize(BusinessTransaction.Builder bt, BtOccurrence.Builder occurrence, String server) throws IOException {
		bt.addOccurrences(occurrence);
		
		Event e = new SimpleEvent();
		e.setBody(bt.build().toByteArray());
		if (server != null) {
			e.getHeaders().put(BtExportHandler.HEADER_KEY_SERVER, server);
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BtExportJSONSerializer serializer = new BtExportJSONSerializer(bos);
		serializer.configure(new Context());
		
		serializer.write(e);
		serializer.flush();
		return new String(bos.toByteArray(), "UTF8");
	}
	
}
