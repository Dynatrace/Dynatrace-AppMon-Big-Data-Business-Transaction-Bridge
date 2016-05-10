package com.dynatrace.diagnostics.flume.btexport;

import java.io.IOException;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;

/**
 * A simple helper class for testing which stores the last appended {@link Event}.
 *
 */
public class DummySink extends EventSink.Base {

	Event lastEvent;
	
	@Override
	public synchronized void append(Event e) throws IOException, InterruptedException {
		super.append(e);
		lastEvent = e;
	}
	
}
