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

public class BtVisitDecorator<S extends EventSink> extends BtDecorator<S> {
	
	
	/**
	 * Constructs a <code>BtVisitDecorator</code>.
	 * 
	 * @param numericDate - if <code>true</code> dates will be added in a numeric format.
	 * @param charsetName - the character encoding for the generated data
	 * 
	 */
	public BtVisitDecorator(S sink, boolean numericDate, String charsetName) {
		super(sink, numericDate, charsetName);
	}

	
	public static SinkDecoBuilder builder() {
		return new SinkDecoBuilder() {
			// construct a new parameterized decorator
			@Override
			public EventSinkDecorator<EventSink> build(Context context, String... argv) {
				Preconditions.checkArgument(argv.length <= 2, "usage: btVisitDecorator or btVisitDecorator(numericDate) or btVisitDecorator(numericDate, characterEncoding)");
				return new BtVisitDecorator<EventSink>(null, argv.length == 0 ? false : Boolean.parseBoolean(argv[0]), argv.length < 2 ? null : argv[1]);
			}
		};
	}

	
	/**
	 * This is a special function used by the SourceFactory to pull in this class
	 * as a plugin decorator.
	 */
	public static List<Pair<String, SinkDecoBuilder>> getDecoratorBuilders() {
		List<Pair<String, SinkDecoBuilder>> builders = new ArrayList<Pair<String, SinkDecoBuilder>>();
		builders.add(new Pair<String, SinkDecoBuilder>("btVisitDecorator", builder()));
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
		if (occurrence.hasVisitId()) {
			sb.append(occurrence.getVisitId());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasStartTime()) {
			appendDate(sb, occurrence.getStartTime());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasEndTime()) {
			appendDate(sb, occurrence.getEndTime());
		}
		sb.append(FIELD_DELIMITER);
		int nrOfSplittings = bt.getDimensionNamesCount();
		for (int i = 0; i < nrOfSplittings; i++) {
			if (i > 0) {
				sb.append(COLLECTION_DELIMITER);
			}
			sb.append(escape(bt.getDimensionNames(i)))
				.append(MAP_KEY_DELIMITER)
				.append(occurrence.getDimensions(i));
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
		if (occurrence.hasUser()) {
			sb.append(occurrence.getUser());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasConverted()) {
			sb.append(occurrence.getConverted());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasApdex()) {
			sb.append(occurrence.getApdex());
		}
		
		// Visit detail data
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasNrOfActions()) {
			sb.append(occurrence.getNrOfActions());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasClientFamily()) {
			sb.append(escape(occurrence.getClientFamily()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasClientIP()) {
			sb.append(escape(occurrence.getClientIP()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasContinent()) {
			sb.append(escape(occurrence.getContinent()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasCountry()) {
			sb.append(escape(occurrence.getCountry()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasCity()) {
			sb.append(escape(occurrence.getCity()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasFailedActions()) {
			sb.append(occurrence.getFailedActions());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasClientErrors()) {
			sb.append(occurrence.getClientErrors());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasExitActionFailed()) {
			sb.append(occurrence.getExitActionFailed());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasBounce()) {
			sb.append(occurrence.getBounce());
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasOsFamily()) {
			sb.append(escape(occurrence.getOsFamily()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasOsName()) {
			sb.append(escape(occurrence.getOsName()));
		}
		sb.append(FIELD_DELIMITER);
		if (occurrence.hasConnectionType()) {
			sb.append(escape(occurrence.getConnectionType()));
		}
		sb.append(FIELD_DELIMITER);
		int convertedByCount = occurrence.getConvertedByCount();
		for (int i = 0; i < convertedByCount; i++) {
			if (i > 0) {
				sb.append(COLLECTION_DELIMITER);
			}
			sb.append(escape(occurrence.getConvertedBy(i)));
		}
	}
	
	
	@Override
	Type getBtType() {
		return BusinessTransaction.Type.VISIT;
	}

}