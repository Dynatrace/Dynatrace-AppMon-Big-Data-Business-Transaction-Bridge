package com.dynatrace.diagnostics.btexport.flume;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.flume.serialization.EventSerializer.Builder;
import org.junit.Test;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtPurePathSerializerTest extends BtSerializerTestBase {

	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set is serialized correctly. 
	 *
	 */
	@Test
	public void testFullSerialization1ElemMaps() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.PUREPATH);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey"}));
		bt.setSystemProfile("sp");
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0}));
		
		occurrence.setFailed(true).setVisitId(1234)
				.setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
			
		assertEquals("btName;btApplication;PT\\=288\\;PA\\=-508867027\\;PS\\=-522660323;2013-01-16 11:05:57.84;splittingKey=splitting;" +
				"measureKey=1.0;true;1234;10.0;9.0;8.0;7.0;6.0;5.0;4.0;sp;srv\n", buildAndSerializeToString(bt, occurrence, "srv"));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set and multiple measures is serialized correctly. 
	 *
	 */
	@Test
	public void testFullSerialization2ElemMaps() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.PUREPATH);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey1", "splittingKey2"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey1", "measureKey2"}));
		bt.setSystemProfile("sp");
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting1", "splitting2"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0, 2.0}));
		
		occurrence.setFailed(false).setVisitId(1234)
				.setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
		assertEquals("btName;btApplication;PT\\=288\\;PA\\=-508867027\\;PS\\=-522660323;2013-01-16 11:05:57.84;splittingKey1=splitting1,splittingKey2=splitting2;" +
				"measureKey1=1.0,measureKey2=2.0;false;1234;10.0;9.0;8.0;7.0;6.0;5.0;4.0;sp;srv\n", buildAndSerializeToString(bt, occurrence, "srv"));
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
		
		assertEquals("btName;;;2013-01-16 11:05:57.84;;;;;;;;;;;;;\n", buildAndSerializeToString(bt, occurrence, null));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt of the wrong type is not serialized. 
	 *
	 */
	@Test
	public void testWrongTypeSerialization() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.VISIT);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
			
		assertTrue(buildAndSerializeToString(bt, occurrence, "srv").isEmpty());
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with no type is not serialized. 
	 *
	 */
	@Test
	public void testNoTypeSerialization() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName");
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
			
		assertTrue(buildAndSerializeToString(bt, occurrence, "srv").isEmpty());
	}

	
	@Override
	protected Builder getSerializerBuilder() {
		return new BtPurePathSerializerBuilder();
	}
}
