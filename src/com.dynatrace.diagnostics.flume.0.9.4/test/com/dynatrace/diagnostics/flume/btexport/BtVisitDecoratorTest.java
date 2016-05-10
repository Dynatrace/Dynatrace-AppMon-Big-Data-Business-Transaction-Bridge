package com.dynatrace.diagnostics.flume.btexport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.EventSink;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtVisitDecoratorTest extends BtDecoratorTestBase {

	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set is handled correctly. 
	 *
	 */
	@Test
	public void testFullSerialization1ElemMaps() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.VISIT);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setVisitId(8589934592L);
		occurrence.setStartTime(1358330757840L).setEndTime(1358330832472L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0}));
		
		occurrence.setNrOfActions(322);
		occurrence.setClientFamily("cfam");
		occurrence.setClientIP("1.2.3.4");
		occurrence.setContinent("Europe");
		occurrence.setCountry("Austria");
		occurrence.setCity("Linz");
		occurrence.setFailedActions(2);
		occurrence.setClientErrors(5);
		occurrence.setExitActionFailed(true);
		occurrence.setBounce(false);
		occurrence.setOsFamily("ofam");
		occurrence.setOsName("onam");
		occurrence.setConnectionType("conty");
		occurrence.addAllConvertedBy(Arrays.asList(new String[]{"conv", "by"}));
		
		occurrence.setUser("btUser").setConverted(true).setApdex(0.75);
		assertEquals("btName;btApplication;8589934592;2013-01-16 11:05:57.84;2013-01-16 11:07:12.472;splittingKey=splitting;" +
				"measureKey=1.0;btUser;true;0.75;322;cfam;1.2.3.4;Europe;Austria;Linz;2;5;true;false;ofam;onam;conty;conv,by", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set and multiple measures is handled correctly. 
	 *
	 */
	@Test
	public void testSerialization2ElemMaps() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.VISIT);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey1", "splittingKey2"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey1", "measureKey2"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setVisitId(8589934592L);
		occurrence.setStartTime(1358330757840L).setEndTime(1358330832472L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting1", "splitting2"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0, 2.0}));
		
		occurrence.setUser("btUser").setConverted(false).setApdex(0.75);
		
		assertEquals("btName;btApplication;8589934592;2013-01-16 11:05:57.84;2013-01-16 11:07:12.472;splittingKey1=splitting1,splittingKey2=splitting2;" +
				"measureKey1=1.0,measureKey2=2.0;btUser;false;0.75;;;;;;;;;;;;;;", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with the minimum required values set is handled correctly. 
	 *
	 */
	@Test
	public void testMinimalSerialization() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.VISIT);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		
		assertEquals("btName;;;2013-01-16 11:05:57.84;;;;;;;;;;;;;;;;;;;;", buildAndSerialize(bt, occurrence));
	}
	
	
	/**
	 *	Verifies that the correct headers are set for the protobuf message. 
	 *
	 */
	@Test
	public void testEventHeaders() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.VISIT);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		assertEquals(BusinessTransaction.Type.VISIT.name(), new String(buildEvent(bt, occurrence).get(BtDecorator.HEADER_KEY_BT_TYPE)));
		assertEquals("btName", new String(buildEvent(bt, occurrence).get(BtDecorator.HEADER_KEY_BT_NAME)));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt of the wrong type is not handled. 
	 *
	 */
	@Test
	public void testWrongTypeSerialization() throws Exception {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.PUREPATH);
		
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
		return BtVisitDecorator.builder();
	}
	
	
	@Test
	public void testAppendDateAsNumber() {
		BtDecorator<EventSink> decorator = (BtDecorator<EventSink>)getDecoBuilder().create(null, new Object[] { "true", null });
		StringBuilder sb = new StringBuilder();
		decorator.appendDate(sb, 123456789000L);
		assertEquals("123456789.000", sb.toString());
	}

}
