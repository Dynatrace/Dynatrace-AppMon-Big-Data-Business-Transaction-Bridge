package com.dynatrace.diagnostics.flume;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;
import com.dynatrace.diagnostics.btexport.flume.BtExportHandler;

public class MetaHandler implements HTTPSourceHandler {

	private static final Map<String, String> ALERT_HEADERS = Collections.singletonMap("btType", "ALERT");
	//private static final Map<String, String> EXPORT_HEADERS = Collections.singletonMap("Type", "BusinessTransaction");
	BtExportHandler export = new BtExportHandler();
	@Override
	public void configure(Context arg0) {
		// TODO Auto-generated method stub
		export.configure(arg0);
	}

	@Override
	public List<Event> getEvents(HttpServletRequest arg0)
			throws HTTPBadRequestException, Exception {
		// TODO Auto-generated method stub
		if (arg0.getRequestURI().endsWith("/alerts"))
		{
			int length = arg0.getContentLength();
			byte[] data = new byte[length];
			int read = 0;
			int total = 0;
			while (read >= 0 && total < length) {
				read = arg0.getInputStream().read(data, total, length - total);
				if (read > 0) {
					total += read;
				}
			}
			Event e = new SimpleEvent();
			e.setBody(data);
			e.setHeaders(ALERT_HEADERS);
			return Collections.singletonList(e);
		}
		
		return export.getEvents(arg0);
	}

}
