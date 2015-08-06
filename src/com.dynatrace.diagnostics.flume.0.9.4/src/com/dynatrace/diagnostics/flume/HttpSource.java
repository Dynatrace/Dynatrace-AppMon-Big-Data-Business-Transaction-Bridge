/**
 * Licensed to Cloudera, Inc. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Cloudera, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynatrace.diagnostics.flume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SourceFactory.SourceBuilder;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSource;
import com.cloudera.util.Pair;
import com.google.common.base.Preconditions;

/**
 * Simple Source that generates a "hello world!" event every 3 seconds.
 */
public class HttpSource extends EventSource.Base {
	
	private static final Logger LOG = LoggerFactory.getLogger(HttpSource.class);
	
	private final Server server;
	
	private final HttpHandler handler;
	
	
	/**
	 * Constructs a new <code>HttpSource</code> with the given port.
	 *
	 * @param port
	 */
	public HttpSource(int port) {
		server = new Server(port);
		handler = new HttpHandler();
		this.server.addHandler(handler);
	}
	
  
	/**
	 * Starts a webserver listening on the specified port, creating one {@link Event} per incoming request.
	 */
	@Override
	public void open() throws IOException {
		try {
			server.start();
			LOG.info("HttpSource started");
		} catch (Exception e) {
			LOG.error("unable to start HttpSource", e);
		}
	}


	/**
	 * @return The next {@link Event}, blocking if none is available.
	 */
	@Override
	public Event next() throws IOException {
		try {
			return handler.takeEvent();
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
			throw new IOException("InterruptedException while retrieving next event", ie);
		}
	}
	

	/**
	 * Stops the webserver.
	 */
	@Override
	public void close() throws IOException {
		// Cleanup
		try {
			server.stop();
			LOG.info("HttpSource stopped");
		} catch (Exception e) {
			LOG.warn("unable to stop HttpSource correctly", e);
		}
	}
	

	public static SourceBuilder builder() {
		// construct a new parameterized source
		return new SourceBuilder() {
			@Override
			public EventSource build(Context ctx,String... argv) {
				Preconditions.checkArgument(argv.length == 1, "usage: httpSource(port)");
				int port = -1;
				try {
					port = Integer.parseInt(argv[0]);
				} catch (NumberFormatException nfe) {
					LOG.error("Port is not a number: '" + argv[0] + "'");
					return null;
				}
				if (port < 0 || port > 65535) {
					LOG.error("Port has to be between 0 and 65535, but was: '" + argv[0] + "'");
					return null;
				}
				return new HttpSource(port);
			}
		};
	}
	

	/**
	 * This is a special function used by the SourceFactory to pull in this class
	 * as a plugin source.
	 */
	public static List<Pair<String, SourceBuilder>> getSourceBuilders() {
		List<Pair<String, SourceBuilder>> builders = new ArrayList<Pair<String, SourceBuilder>>();
		builders.add(new Pair<String, SourceBuilder>("httpSource", builder()));
		return builders;
	}
}