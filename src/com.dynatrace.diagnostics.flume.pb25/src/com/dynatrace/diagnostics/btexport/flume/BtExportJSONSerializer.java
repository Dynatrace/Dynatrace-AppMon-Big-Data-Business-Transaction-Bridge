package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.conf.Configurable;
import org.apache.flume.serialization.EventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class BtExportJSONSerializer implements EventSerializer, Configurable {

	private final static Logger log = LoggerFactory
			.getLogger(BtExportJSONSerializer.class);
	public static final String CONFIG_CHARSET = "charset";
	private final OutputStream out;
	private final Gson gson;
	private OutputStreamWriter outWriter;

	private final DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SZ");

	public BtExportJSONSerializer(OutputStream out) {
		this.out = out;
		GsonBuilder g = new GsonBuilder();
		gson = g.create();
	}

	@Override
	public void configure(Context context) {
		String charset = context.getString(CONFIG_CHARSET, "UTF8");
		try {
			outWriter = new OutputStreamWriter(out, charset);
			if (log.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder(80);
				sb.append(this.getClass().getSimpleName())
						.append(": Configured serializer with encoding: '")
						.append(charset).append('\'');
				log.info(sb.toString());
			}
		} catch (UnsupportedEncodingException uee) {
			StringBuilder sb = new StringBuilder(80);
			sb.append(this.getClass().getSimpleName())
					.append(": Unsupported encoding, disabling serializer: '")
					.append(charset).append('\'');
			log.error(sb.toString(), uee);
		}

	}

	@Override
	public void afterCreate() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterReopen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		outWriter.flush();

	}

	@Override
	public boolean supportsReopen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void write(Event event) throws IOException {
		BusinessTransaction bt = BusinessTransaction.parseFrom(event.getBody());
		if (!bt.hasType()) {
			log.warn("Skipping serialization of event without type: btName='"
					+ bt.getName() + "'");
			return;
		}
		for (BtOccurrence occurrence : bt.getOccurrencesList()) {
			JsonObject jsonElement = new JsonObject();

			if (bt.hasName()) {
				jsonElement.addProperty("name", bt.getName());
			}
			if (bt.hasApplication()) {
				jsonElement.addProperty("application", bt.getApplication());
			}
			if (bt.hasSystemProfile()) {
				jsonElement.addProperty("systemProfile", bt.getSystemProfile());
			}
			String server = event.getHeaders().get(BtExportHandler.HEADER_KEY_SERVER);
			if (server != null) {
				jsonElement.addProperty("server", server);
			}
			
			if (bt.hasType()) {
				jsonElement.addProperty("type", bt.getType().name());
			}

			if (occurrence.hasPurePathId()) {
				jsonElement.addProperty("purePathId", occurrence.getPurePathId());
			}

			if (occurrence.hasStartTime()) {
				jsonElement.addProperty("startTime", dateFormat.format(new Date(occurrence.getStartTime())));
			}

			if (occurrence.hasEndTime()) {
				jsonElement.addProperty("endTime", dateFormat.format(new Date(occurrence.getEndTime())));
			}

			final int nrOfSplittings = bt.getDimensionNamesCount();
			if (nrOfSplittings > 0) {
				final JsonObject dimensions = new JsonObject();
				// safety net, in case the number of dimensions changed in between
				final int realSize = occurrence.getDimensionsCount();
				for (int i = 0; i < nrOfSplittings && i < realSize ; i++) {
					dimensions.addProperty(bt.getDimensionNames(i),	occurrence.getDimensions(i));
				}
				jsonElement.add("dimensions", dimensions);
			}
			
			final int nrOfMeasures = bt.getMeasureNamesCount();
			if (nrOfMeasures > 0) {
				final JsonObject measures = new JsonObject();
				// safety net, in case the number of measures changed in between
				final int realSize = occurrence.getValuesCount();
				for (int i = 0; i < nrOfMeasures && i < realSize; i++) {
					measures.addProperty(bt.getMeasureNames(i),	occurrence.getValues(i));
				}

				jsonElement.add("measures", measures);
			}

			if (occurrence.hasFailed()) {
				jsonElement.addProperty("failed", occurrence.getFailed());
			}

			if (occurrence.hasVisitId()) {
				jsonElement.addProperty("visitId", occurrence.getVisitId());
			}

			if (occurrence.hasActionName()) {
				jsonElement.addProperty("actionName", occurrence.getActionName());
			}

			if (occurrence.hasApdex()) {
				jsonElement.addProperty("apdex", occurrence.getApdex());
			}

			if (occurrence.hasConverted()) {
				jsonElement.addProperty("converted", occurrence.getConverted());
			}

			if (occurrence.hasQuery()) {
				jsonElement.addProperty("query", occurrence.getQuery());
			}

			if (occurrence.hasUrl()) {
				jsonElement.addProperty("url", occurrence.getUrl());
			}

			if (occurrence.hasUser()) {
				jsonElement.addProperty("user", occurrence.getUser());
			}

			if (occurrence.hasResponseTime()) {
				jsonElement.addProperty("responseTime", occurrence.getResponseTime());
			}
			if (occurrence.hasDuration()) {
				jsonElement.addProperty("duration", occurrence.getDuration());
			}

			if (occurrence.hasCpuTime()) {
				jsonElement.addProperty("cpuTime", occurrence.getCpuTime());
			}

			if (occurrence.hasExecTime()) {
				jsonElement.addProperty("execTime", occurrence.getExecTime());
			}

			if (occurrence.hasSuspensionTime()) {
				jsonElement.addProperty("suspensionTime", occurrence.getSuspensionTime());
			}

			if (occurrence.hasSyncTime()) {
				jsonElement.addProperty("syncTime", occurrence.getSyncTime());
			}

			if (occurrence.hasWaitTime()) {
				jsonElement.addProperty("waitTime", occurrence.getWaitTime());
			}
			
			if (occurrence.hasNrOfActions()) {
				jsonElement.addProperty("nrOfActions", occurrence.getNrOfActions());
			}
			
			if (occurrence.hasClientFamily()) {
				jsonElement.addProperty("clientFamily", occurrence.getClientFamily());
			}
			
			if (occurrence.hasClientIP()) {
				jsonElement.addProperty("clientIP", occurrence.getClientIP());
			}
			
			if (occurrence.hasContinent()) {
				jsonElement.addProperty("continent", occurrence.getContinent());
			}
			
			if (occurrence.hasCountry()) {
				jsonElement.addProperty("country", occurrence.getCountry());
			}
			
			if (occurrence.hasCity()) {
				jsonElement.addProperty("city", occurrence.getCity());
			}
			
			if (occurrence.hasFailedActions()) {
				jsonElement.addProperty("failedActions", occurrence.getFailedActions());
			}
			
			if (occurrence.hasClientErrors()) {
				jsonElement.addProperty("clientErrors", occurrence.getClientErrors());
			}
			
			if (occurrence.hasExitActionFailed()) {
				jsonElement.addProperty("exitActionFailed", occurrence.getExitActionFailed());
			}
			
			if (occurrence.hasBounce()) {
				jsonElement.addProperty("bounce", occurrence.getBounce());
			}
			
			if (occurrence.hasOsFamily()) {
				jsonElement.addProperty("osFamily", occurrence.getOsFamily());
			}
			
			if (occurrence.hasOsName()) {
				jsonElement.addProperty("osName", occurrence.getOsName());
			}
			
			if (occurrence.hasConnectionType()) {
				jsonElement.addProperty("connectionType", occurrence.getConnectionType());
			}
			
			final int nrOfConvertedBy = occurrence.getConvertedByCount();
			if (nrOfConvertedBy > 0) {
				final JsonArray convertedBy = new JsonArray();
				for (int i = 0; i < nrOfConvertedBy; i++) {
					convertedBy.add(new JsonPrimitive(occurrence.getConvertedBy(i)));
				}
				jsonElement.add("convertedBy", convertedBy);
			}
			outWriter.write(gson.toJson(jsonElement));
			outWriter.write('\n');
		}


	}

}
