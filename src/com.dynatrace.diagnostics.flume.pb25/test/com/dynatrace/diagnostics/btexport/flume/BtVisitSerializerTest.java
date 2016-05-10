package com.dynatrace.diagnostics.btexport.flume;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.flume.serialization.EventSerializer.Builder;
import org.junit.Test;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtVisitSerializerTest extends BtSerializerTestBase {

	/**
	 *	Verifies that a protobuf message containing one bt with all possible values set is serialized correctly. 
	 *
	 */
	@Test
	public void testFullSerialization1ElemMaps() throws IOException {
		
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
		
		bt.setSystemProfile("sp");
				
		assertEquals("btName;btApplication;8589934592;2013-01-16 11:05:57.84;2013-01-16 11:07:12.472;splittingKey=splitting;" +
				"measureKey=1.0;btUser;true;0.75;322;cfam;1.2.3.4;Europe;Austria;Linz;2;5;true;false;ofam;onam;conty;conv,by;sp;srv\n", buildAndSerializeToString(bt, occurrence, "srv"));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with multiple measures is serialized correctly. 
	 *
	 */
	@Test
	public void testSerialization2ElemMaps() throws IOException {
		
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
				"measureKey1=1.0,measureKey2=2.0;btUser;false;0.75;;;;;;;;;;;;;;;;srv\n", buildAndSerializeToString(bt, occurrence, "srv"));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt with the minimum required values set is serialized correctly. 
	 *
	 */
	@Test
	public void testMinimalSerialization() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.VISIT);
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setStartTime(1358330757840L);
		
		assertEquals("btName;;;2013-01-16 11:05:57.84;;;;;;;;;;;;;;;;;;;;;;\n", buildAndSerializeToString(bt, occurrence, null));
	}
	
	
	/**
	 *	Verifies that a protobuf message containing one bt of the wrong type is not serialized. 
	 *
	 */
	@Test
	public void testWrongTypeSerialization() throws IOException {
		
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setType(BusinessTransaction.Type.PUREPATH);
		
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
		return new BtVisitSerializerBuilder();
	}
}
