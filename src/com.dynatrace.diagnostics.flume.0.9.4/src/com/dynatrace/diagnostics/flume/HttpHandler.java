package com.dynatrace.diagnostics.flume;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.AbstractHandler;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;

public class HttpHandler extends AbstractHandler {
	
	public static final String HEADER_KEY_BT_TYPE = "btType";
	public static final String HEADER_KEY_BT_NAME = "btName";
	
	
	final static Logger log = Logger.getLogger(HttpHandler.class.getName());
	
	private static final LinkedBlockingQueue<Event> QUEUE = new LinkedBlockingQueue<Event>(100000);
	
	
	/**
	 * Creates an {@link Event} containing the data of the given {@link HttpServletRequest} as body. 
	 */
	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
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
		QUEUE.add(new EventImpl(data));
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		Request base_request = (request instanceof Request) ? (Request)request : HttpConnection.getCurrentConnection().getRequest();
		base_request.setHandled(true);
	}
	
	/**
	 * blocks until an {@link Event} is available.
	 * @return
	 * @throws InterruptedException
	 */
	public Event takeEvent() throws InterruptedException {
		return QUEUE.take();
	}
	
	
	/**
	 * used for testing to avoid infinite blocking
	 *
	 * @return
	 */
	boolean hasEvent() {
		return !QUEUE.isEmpty();
	}

}
