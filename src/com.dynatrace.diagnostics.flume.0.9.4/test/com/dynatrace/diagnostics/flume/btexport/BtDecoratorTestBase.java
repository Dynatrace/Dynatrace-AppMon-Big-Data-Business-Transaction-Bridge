package com.dynatrace.diagnostics.flume.btexport;

import java.io.IOException;

import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;

public abstract class BtDecoratorTestBase {

	
	/**
	 * Builds an {@link Event} using the Decorator built by the {@link SinkDecoBuilder} returned by {@link #getDecoBuilder()} as implemented by any subclass.
	 * The {@link Event} contains the data of the given {@link BtOccurrence.Builder}.
	 * @param bt
	 * @param occurrence
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected Event buildEvent(BusinessTransaction.Builder bt, BtOccurrence.Builder occurrence) throws IOException, InterruptedException {
		BusinessTransactions.Builder bts = BusinessTransactions.newBuilder();
		bt.addOccurrences(occurrence);
		bts.addBusinessTransactions(bt);
		Event e = new DummyEvent(bts.build().toByteArray());
		EventSinkDecorator<EventSink> decorator = getDecoBuilder().create(null, new Object[] {});
		DummySink dummySink = new DummySink();
		decorator.setSink(dummySink);
		decorator.open();
		decorator.append(e);
		decorator.close();
		return dummySink.lastEvent;
	}
	
	
	/**
	 * Transforms the given {@link BtOccurrence.Builder} to a {@link String} using the {@link SinkDecoBuilder} returned by {@link #getDecoBuilder()} as implemented by any subclass
	 *
	 * @param bt
	 * @param occurrence
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected String buildAndSerialize(BusinessTransaction.Builder bt, BtOccurrence.Builder occurrence) throws IOException, InterruptedException {
		Event e = buildEvent(bt, occurrence);
		if (e == null || e.getBody() == null) {
			return null;
		}
		return new String(e.getBody());
	}
	
	
	/**
	 *
	 * @return A {@link SinkDecoBuilder} to create the {@link BtDecorator}-implementation to be tested.
	 */
	abstract SinkDecoBuilder getDecoBuilder();
	
}
