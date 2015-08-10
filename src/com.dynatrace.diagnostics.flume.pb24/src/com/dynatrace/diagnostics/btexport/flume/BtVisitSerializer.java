package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.flume.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;

public class BtVisitSerializer extends BtSerializer {
	
	
	final static Logger log = LoggerFactory.getLogger(BtVisitSerializer.class);

	
	/**
	 * Constructs a <code>BtVisitSerializer</code> that serializes {@link Event}s containing Visit Business Transaction data to the given {@link OutputStream}.
	 * @param out
	 */
	public BtVisitSerializer(OutputStream out) {
		super(out);
	}

	
	/**
	 * Writes the given {@link Event} to the {@link OutputStream}, if its type matches the type of this Serializer.
	 * One line is written per {@link BtOccurrence} included in the event. To ensure usability by hive strings are escaped using
	 * {@link BtSerializer#escape(String)}.
	 * @param - event the {@link Event} to be serialized
	 * @see BusinessTransaction.Type
	 */
	@Override
	public void write(Event event) throws IOException {
		BusinessTransaction bt = BusinessTransaction.parseFrom(event.getBody());
		
		if (!bt.hasType()) {
			log.warn("Skipping serialization of event without type: btName='" + bt.getName() + "'");
			return;
		}
		if (bt.getType() != BusinessTransaction.Type.VISIT) {
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
			sb.append(LINE_DELIMITER);
			write(sb.toString());
		}
	}

}
