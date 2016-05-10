package com.dynatrace.diagnostics.flume.btexport;

import com.cloudera.flume.core.EventBaseImpl;

/**
 * A helper class for testing simply wrapping a <code>byte[]</code> as event body.
 */
public class DummyEvent extends EventBaseImpl {
	
	final byte[] body;
	
	public DummyEvent(byte[] body) {
		this.body = body;
	}

	@Override
	public byte[] getBody() {
		return body;
	}

	@Override
	public String getHost() {
		return null;
	}

	@Override
	public long getNanos() {
		return 0;
	}

	@Override
	public Priority getPriority() {
		return null;
	}

	@Override
	public long getTimestamp() {
		return 0;
	}

}
