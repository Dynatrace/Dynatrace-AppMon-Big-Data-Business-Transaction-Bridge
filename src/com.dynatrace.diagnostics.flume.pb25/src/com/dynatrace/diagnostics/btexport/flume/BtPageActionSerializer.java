package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtPageActionSerializer extends BtSerializer {
	
	final static Logger log = LoggerFactory.getLogger(BtPageActionSerializer.class);
	
	
	/**
	 * Constructs a <code>BtPageActionSerializer</code> that serializes {@link Event}s containing Page Action Business Transaction data to the given {@link OutputStream}.
	 * @param out
	 */
	public BtPageActionSerializer(OutputStream out) {
		super(out);
	}

	
	/**
	 * Writes the given {@link Event} to the {@link OutputStream}, if its type matches the type of this Serializer.
	 * One line is written per {@link BtOccurrence} included in the event. To ensure usability by hive strings are escaped using
	 * {@link BtSerializer#escape(String)}.
	 * @param event - the {@link Event} to be serialized
	 * @see BusinessTransaction.Type
	 */
	@Override
	public void write(Event event) throws IOException {
		BusinessTransaction bt = BusinessTransaction.parseFrom(event.getBody());
		
		if (!bt.hasType()) {
			log.warn("Skipping serialization of event without type: btName='" + bt.getName() + "'");
			return;
		}
		if (bt.getType() != BusinessTransaction.Type.USER_ACTION) {
			log.warn("Skipping serialization of event of wrong type: '" + bt.getType() + "', btName: '" + bt.getName() + "'");
			return;
		}
		
		StringBuilder sb = new StringBuilder(256);
		for (BtOccurrence occurrence : bt.getOccurrencesList()) {
			sb.setLength(0);
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
			if (occurrence.hasActionName()) {
				sb.append(escape(occurrence.getActionName()));
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasUrl()) {
				sb.append(escape(occurrence.getUrl()));
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
			
			// Page action detail data
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasClientErrors()) {
				sb.append(occurrence.getClientErrors());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasClientTime()) {
				sb.append(occurrence.getClientTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasNetworkTime()) {
				sb.append(occurrence.getNetworkTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasServerTime()) {
				sb.append(occurrence.getServerTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasUrlRedirectionTime()) {
				sb.append(occurrence.getUrlRedirectionTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasDnsTime()) {
				sb.append(occurrence.getDnsTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasConnectTime()) {
				sb.append(occurrence.getConnectTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasSslTime()) {
				sb.append(occurrence.getSslTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasDocumentRequestTime()) {
				sb.append(occurrence.getDocumentRequestTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasDocumentResponseTime()) {
				sb.append(occurrence.getDocumentResponseTime());
			}
			sb.append(FIELD_DELIMITER);
			if (occurrence.hasProcessingTime()) {
				sb.append(occurrence.getProcessingTime());
			}
			sb.append(LINE_DELIMITER);
			write(sb.toString());
		}
	}

}
