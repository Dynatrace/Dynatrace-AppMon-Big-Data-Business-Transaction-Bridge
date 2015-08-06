package com.dynatrace.diagnostics.flume.btexport;

import java.util.ArrayList;
import java.util.List;

import com.cloudera.flume.conf.Context;
import com.cloudera.flume.conf.SinkFactory.SinkDecoBuilder;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.cloudera.util.Pair;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction.Type;
import com.google.common.base.Preconditions;

public class BtPurePathDecorator<S extends EventSink> extends BtDecorator<S> {
	
	
	/**
	 * Constructs a <code>BtPurePathDecorator</code>.
	 * 
	 * @param numericDate - if <code>true</code> dates will be added in a numeric format.
	 * @param charsetName - the character encoding for the generated data
	 * 
	 */
	public BtPurePathDecorator(S sink, boolean numericDate, String charsetName) {
		super(sink, numericDate, charsetName);
	}

  
	public static SinkDecoBuilder builder() {
		return new SinkDecoBuilder() {
			// construct a new parameterized decorator
			@Override
			public EventSinkDecorator<EventSink> build(Context context, String... argv) {
				Preconditions.checkArgument(argv.length <= 2, "usage: btPurePathDecorator or btPurePathDecorator(numericDate) or btPurePathDecorator(numericDate, characterEncoding)");
				return new BtPurePathDecorator<EventSink>(null, argv.length == 0 ? false : Boolean.parseBoolean(argv[0]), argv.length < 2 ? null : argv[1]);
			}
		};
	}
	

	/**
	 * This is a special function used by the SourceFactory to pull in this class
	 * as a plugin decorator.
	 */
	public static List<Pair<String, SinkDecoBuilder>> getDecoratorBuilders() {
		List<Pair<String, SinkDecoBuilder>> builders = new ArrayList<Pair<String, SinkDecoBuilder>>();
		builders.add(new Pair<String, SinkDecoBuilder>("btPurePathDecorator", builder()));
		return builders;
	}

	
	@Override
	void appendOccurrence(StringBuilder sb, BusinessTransaction bt, BtOccurrence occurrence) {
		if (bt.hasName()) {
			sb.append(escape(bt.getName()));
		}
		sb.append(FIELD_DELIMITER);
		if (bt.hasApplication()) {
			sb.append(escape(bt.getApplication()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasPurePathId()) {
			sb.append(escape(occurrence.getPurePathId()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasStartTime()) {
			appendDate(sb, occurrence.getStartTime());
		}
		sb.append(FIELD_DELIMITER);
		int nrOfSplittings = bt.getDimensionNamesCount();
		for (int i = 0; i < nrOfSplittings; i++) {
			if (i > 0) {
				sb.append(COLLECTION_DELIMITER);
			}
			sb.append(escape(bt.getDimensionNames(i)))
				.append(MAP_KEY_DELIMITER)
				.append(escape(occurrence.getDimensions(i)));
			}
		sb.append(FIELD_DELIMITER);
		int nrOfMeasures = bt.getMeasureNamesCount();
		for (int i = 0; i < nrOfMeasures; i++) {
			if (i > 0) {
				sb.append(COLLECTION_DELIMITER);
			}
			sb.append(escape(bt.getMeasureNames(i)))
				.append(MAP_KEY_DELIMITER)
				.append(occurrence.getValues(i));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasFailed()) {
			sb.append(occurrence.getFailed());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasVisitId()) {
			sb.append(occurrence.getVisitId());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasResponseTime()) {
			sb.append(occurrence.getResponseTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasDuration()) {
			sb.append(occurrence.getDuration());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasCpuTime()) {
			sb.append(occurrence.getCpuTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasExecTime()) {
			sb.append(occurrence.getExecTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasSuspensionTime()) {
			sb.append(occurrence.getSuspensionTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasSyncTime()) {
			sb.append(occurrence.getSyncTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasWaitTime()) {
			sb.append(occurrence.getWaitTime());
		}
	}


	@Override
	Type getBtType() {
		return BusinessTransaction.Type.PUREPATH;
	}
}