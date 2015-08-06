package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * 
 * 
 *  
 */
public class BtExportHandler implements HTTPSourceHandler {
	
	public static final String HEADER_KEY_BT_TYPE = "btType";
	public static final String HEADER_KEY_BT_NAME = "btName";
	
	/**
	 * Does nothing.
	 */
	@Override
	public void configure(Context context) {
		//noop
	}

	/**
	 * Creates an Event for each BusinessTransaction that's included in the request data and returns them as a List.
	 * 
	 * @param request
	 * @return A {@link List} of {@link Event}s. Never returns <code>null</code>
	 */
	@Override
	public List<Event> getEvents(HttpServletRequest request) throws HTTPBadRequestException, IOException  {
		int length = request.getContentLength();
		byte[] data = new byte[length];
		int read = 0;
		int total = 0;
		while (read >= 0 && total < length) {
			read = request.getInputStream().read(data, total, length - total);
			if (read > 0) {
				total += read;
			}
		}
		try {
			BusinessTransactions bts = BusinessTransactions.parseFrom(data);
			List<Event> events = new ArrayList<Event>(bts.getBusinessTransactionsCount());
			
			for (BusinessTransaction bt : bts.getBusinessTransactionsList()) {
				Event event = EventBuilder.withBody(bt.toByteArray());
				Map<String, String> headers = new TreeMap<String, String>();
				headers.put(HEADER_KEY_BT_TYPE, bt.getType().name());
				headers.put(HEADER_KEY_BT_NAME, bt.getName());
				event.setHeaders(headers);
				events.add(event);
			}
			return events;
		} catch (InvalidProtocolBufferException ipbe) {
			throw new HTTPBadRequestException(ipbe);
		}
	}

}
