package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtPurePathSerializer extends BtSerializer {
	
	
	final static Logger log = LoggerFactory.getLogger(BtPurePathSerializer.class);
	
	
	/**
	 * Constructs a <code>BtPurePathSerializer</code> that serializes {@link Event}s containing PurePath Business Transaction data to the given {@link OutputStream}.
	 * @param out
	 */
	BtPurePathSerializer(OutputStream out) {
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
		if (bt.getType() != BusinessTransaction.Type.PUREPATH) {
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
			sb.append(FIELD_DELIMITER);
			if (bt.hasSystemProfile()) {
				sb.append(bt.getSystemProfile());
			}
			sb.append(FIELD_DELIMITER);
			String server = event.getHeaders().get(BtExportHandler.HEADER_KEY_SERVER);
			if (server != null) {
				sb.append(server);
			}
			sb.append(LINE_DELIMITER);
			write(sb.toString());
		}
	}

}
