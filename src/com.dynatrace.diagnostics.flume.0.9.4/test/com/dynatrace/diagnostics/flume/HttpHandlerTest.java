package com.dynatrace.diagnostics.flume;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mortbay.jetty.Response;

import com.cloudera.flume.core.Event;


public class HttpHandlerTest {

	
	@Test
	public void testHandle() throws Exception {
		HttpHandler handler = new HttpHandler();
		byte[] data = new byte[] {1, 3, 5, 7, 9, 10};
		handler.handle(null, new DummyRequest(data), new Response(null) {
			@Override
			public void setStatus(int sc) {
				//do nothing
			}
		}, 0);
		
		assertTrue(handler.hasEvent());
		Event e = handler.takeEvent();
		assertArrayEquals(data, e.getBody());
	}
}
