package com.dynatrace.diagnostics.flume.btexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.EventSink;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtPurePathDecoratorTest extends BtDecoratorTestBase {

	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set is handled correctly. 
	 *
	 */
	@Test
	public void testFullSerialization1ElemMaps() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.PUREPATH);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0}));
		occurrence.setVisitId(1234);
		
		occurrence.setFailed(true).setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
			
		assertEquals("btName;btApplication;PT\\=288\\;PA\\=-508867027\\;PS\\=-522660323;2013-01-16 11:05:57.84;splittingKey=splitting;" +
				"measureKey=1.0;true;1234;10.0;9.0;8.0;7.0;6.0;5.0;4.0", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set and multiple measures is handled correctly. 
	 *
	 */
	@Test
	public void testFullSerialization2ElemMaps() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.PUREPATH);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey1", "splittingKey2"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey1", "measureKey2"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting1", "splitting2"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0, 2.0}));
		occurrence.setVisitId(1234);
		
		occurrence.setFailed(false).setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
		assertEquals("btName;btApplication;PT\\=288\\;PA\\=-508867027\\;PS\\=-522660323;2013-01-16 11:05:57.84;splittingKey1=splitting1,splittingKey2=splitting2;" +
				"measureKey1=1.0,measureKey2=2.0;false;1234;10.0;9.0;8.0;7.0;6.0;5.0;4.0", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with the minimum required values set is handled correctly. 
	 *
	 */
	@Test
	public void testMinimalSerialization() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.PUREPATH);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		
		assertEquals("btName;;;2013-01-16 11:05:57.84;;;;;;;;;;;", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that the correct headers are set for the protobuf message. 
	 *
	 */
	@Test
	public void testEventHeaders() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.PUREPATH);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		assertEquals(BusinessTransaction.Type.PUREPATH.name(), new String(buildEvent(bt, occurrence).get(BtDecorator.HEADER_KEY_BT_TYPE)));
		assertEquals("btName", new String(buildEvent(bt, occurrence).get(BtDecorator.HEADER_KEY_BT_NAME)));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt of the wrong type is not handled. 
	 *
	 */
	@Test
	public void testWrongTypeSerialization() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.VISIT);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
			
		assertNull(buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with no type is not handled. 
	 *
	 */
	@Test
	public void testNoTypeSerialization() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName");
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
			
		assertNull(buildAndSerialize(bt, occurrence));
	}

	
	@Override
	SinkDecoBuilder getDecoBuilder() {
		return BtPurePathDecorator.builder();
	}
	
	
	@Test
	public void testAppendDateAsNumber() {
		BtDecorator<EventSink> decorator = (BtDecorator<EventSink>)getDecoBuilder().create(null, new Object[] { "true", null });
		StringBuilder sb = new StringBuilder();
		decorator.appendDate(sb, 123456789000L);
		assertEquals("123456789.000", sb.toString());
	}
	
}
