package com.dynatrace.diagnostics.flume;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.mortbay.jetty.Request;

/**
 * <p>
 * A dummy implementation of {@link HttpServletRequest} simply wrapping a <code>byte[]</code>.
 * The methods implemented are:
 * </p>
 * <ul>
 *   <li>{@link #getContentLength()}</li>
 *   <li>{@link #getInputStream()}</li>
 * </ul>
 */
class DummyRequest extends Request {
	
	private final byte[] data;
	
	
	public DummyRequest(byte[] data) {
		this.data = data;
	}
	
	
	/**
	 * @return The length of the wrapped <code>byte[]</code>.
	 */
	@Override
	public int getContentLength() {
		return data.length;
	}

	
	/**
	 * @return A {@link ServletInputStream} returning the data in the wrapped <code>byte[]</code>.
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ServletInputStream() {
			
			final ByteArrayInputStream bais = new ByteArrayInputStream(data);
			
			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}
	
}