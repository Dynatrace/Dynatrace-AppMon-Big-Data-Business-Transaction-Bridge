package com.dynatrace.diagnostics.flume.testdata;


import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;

public class TestDataSender {

	static void exportPurePath(String url) throws Exception {
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.PUREPATH);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0}));
		
		occurrence.setFailed(true).setVisitId(1234)
				.setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
		bt.addOccurrences(occurrence);
		
		BusinessTransactions.Builder bts = BusinessTransactions.newBuilder();
		bts.addBusinessTransactions(bt.build());
		
		doExport(url, bts.build().toByteArray());
		System.err.println("exported purepath bt successfully");
	}




	static void exportVisit(String url) throws Exception {
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
				
		bt.addOccurrences(occurrence);
		
		BusinessTransactions.Builder bts = BusinessTransactions.newBuilder();
		bts.addBusinessTransactions(bt.build());
		
		doExport(url, bts.build().toByteArray());
		System.err.println("exported visit bt successfully");
	}




	static void exportPageAction(String url) throws Exception {
		BusinessTransaction.Builder bt = BusinessTransaction.newBuilder();
		bt.setName("btName").setApplication("btApplication").setType(BusinessTransaction.Type.USER_ACTION);
		bt.addAllDimensionNames(Arrays.asList(new String[] {"splittingKey"}));
		bt.addAllMeasureNames(Arrays.asList(new String[] {"measureKey"}));
		
		BtOccurrence.Builder occurrence = BtOccurrence.newBuilder();
		occurrence.setPurePathId("PT=288;PA=-508867027;PS=-522660323");
		occurrence.setStartTime(1358330757840L);
		occurrence.addAllDimensions(Arrays.asList(new String[] {"splitting"}));
		occurrence.addAllValues(Arrays.asList(new Double[] {1.0}));
		
		occurrence.setFailed(false).setActionName("btActionName").setUrl("http://someurl.com")
				.setVisitId(8589934592L).setResponseTime(10.0).setDuration(9.0).setCpuTime(8.0)
				.setExecTime(7.0).setSuspensionTime(6.0).setSyncTime(5.0).setWaitTime(4.0);
		
		occurrence.setClientErrors(10);
		occurrence.setClientTime(11.0);
		occurrence.setNetworkTime(12.0);
		occurrence.setServerTime(13.0);
		occurrence.setUrlRedirectionTime(14);
		occurrence.setDnsTime(15);
		occurrence.setConnectTime(16);
		occurrence.setSslTime(17);
		occurrence.setDocumentRequestTime(18);
		occurrence.setDocumentResponseTime(19);
		occurrence.setProcessingTime(20);
		
		bt.addOccurrences(occurrence);
		
		BusinessTransactions.Builder bts = BusinessTransactions.newBuilder();
		bts.addBusinessTransactions(bt.build());
		
		doExport(url, bts.build().toByteArray());
		System.err.println("exported page action bt successfully");
	}
	
	
	
	
	public static void doExport(String exportUrl, byte[] data) throws Exception {
		HttpURLConnection connection = null;
		URL url = new URL(exportUrl);
		connection = (HttpURLConnection)url.openConnection();
		connection.setConnectTimeout(20000);
		connection.setReadTimeout(20000);
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/octet-stream");
		connection.setRequestProperty("Content-Length", String.valueOf(data.length));
		DataOutputStream os = new DataOutputStream(connection.getOutputStream());
		os.write(data);
		os.flush();
		os.close();
		Reader reader = new InputStreamReader(connection.getInputStream());
		while (reader.read() > -1) {
			//have to read all from the inputstream, so the connection can be reused
		}
		reader.close();
		connection.disconnect();
	}

}
