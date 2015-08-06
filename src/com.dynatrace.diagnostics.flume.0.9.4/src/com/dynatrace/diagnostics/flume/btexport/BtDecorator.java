package com.dynatrace.diagnostics.flume.btexport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloudera.flume.core.Event;
import com.cloudera.flume.core.EventImpl;
import com.cloudera.flume.core.EventSink;
import com.cloudera.flume.core.EventSinkDecorator;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BtOccurrence;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransaction;
import com.dynatrace.diagnostics.core.realtime.export.BtExport.BusinessTransactions;
import com.google.protobuf.InvalidProtocolBufferException;

public abstract class BtDecorator<S extends EventSink> extends EventSinkDecorator<S> {
	
	private static final Logger LOG = LoggerFactory.getLogger(BtDecorator.class);
	
	public static final String HEADER_KEY_BT_TYPE = "btType";
	public static final String HEADER_KEY_BT_NAME = "btName";
	
	static final char FIELD_DELIMITER = ';';

	static final char MAP_KEY_DELIMITER = '=';

	static final char COLLECTION_DELIMITER = ',';
	
	static final char LINE_DELIMITER = '\n';
	
	static final char LINE_DELIMITER_ESCAPED = 'n';
	
	static final char ESCAPE_CHAR = '\\';
	
	private final Charset charset;
	
	private final boolean numericDate;

	
	/**
	 * Constructs a BtDecorator, which splits incoming {@link Event}s containing a {@link BusinessTransactions} protobuf message.
	 * For each {@link BusinessTransaction} within the {@link Event} that has the correct type according to an implemenation of this
	 * class an {@link Event} will be created. The body of the created {@link Event} is a csv formatted {@link String} representing the {@link BusinessTransaction}.
	 * 
	 * @param sink
	 * @param charsetName - allows to specify a charset to encode the event body with. May be <code>null</code> in which case the platform's default charset will be used.
	 */
	public BtDecorator(S sink, boolean numericDate, String charsetName) {
		super(sink);
		if (charsetName == null || charsetName.isEmpty()) {
			this.charset = Charset.defaultCharset();
		} else {
			this.charset = Charset.forName(charsetName);
			if (LOG.isInfoEnabled()) {
				StringBuilder sb = new StringBuilder(80);
				sb.append(getClass().getSimpleName())
					.append(": Configured decorator with encoding: '")
					.append(charset)
					.append('\'');
				LOG.info(sb.toString());
			}
		}
		this.numericDate = numericDate;
	}
	
	
	/**
	 * Creates an {@link Event} for every {@link BusinessTransaction} in the given {@link Event} with the same type as returned by {@link #getBtType()}.
	 * The body of the created events is a csv text with each line representing an occurrence of the Business Transaction.
	 * @param event - the {@link Event} containing the {@link BusinessTransactions} to be split up. 
	 */
	@Override
	public void append(Event event) throws IOException, InterruptedException {
		try {
			BusinessTransactions bts = BusinessTransactions.parseFrom(event.getBody());
			
			for (BusinessTransaction bt : bts.getBusinessTransactionsList()) {
				String s = btToString(bt);
				if (s != null && !s.isEmpty()) {
					Event e = new EventImpl(s.getBytes(charset));
					e.set(HEADER_KEY_BT_TYPE, bt.getType().name().getBytes());
					e.set(HEADER_KEY_BT_NAME, bt.getName().getBytes());
					super.append(e);
				}
			}
		} catch (InvalidProtocolBufferException ipbe) {
			LOG.warn("Exception parsing protobuf message", ipbe);
		}
	}
	
	
	/**
	 * If the type of the given {@link BusinessTransaction} equals the type as returned by {@link #getBtType()} for every 
	 * occurrence of the Business Transaction a line will be created in the returned String. 
	 *
	 * @param bt - the {@link BusinessTransaction} to be written.
	 * @return A {@link String} with a single line for each occurrence of the given {@link BusinessTransaction}.
	 * Returns <code>null</code> if the bt cannot be handled by this decorator. 
	 */
	String btToString(BusinessTransaction bt) {
		if (!bt.hasType()) {
			LOG.warn("Skipping serialization of event without type: btName='" + bt.getName() + "'");
			return null;
		}
		if (bt.getType() != getBtType()) {
			return null;
		}
		if (bt.getOccurrencesCount() == 0) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder(256);
		for (BtOccurrence occurrence : bt.getOccurrencesList()) {
			if (sb.length() > 0) {
				sb.append(LINE_DELIMITER);
			}
			appendOccurrence(sb, bt, occurrence);
		}
		return sb.toString();
	}
	
	
	/**
	 * Appends a line representing the given {@link BtOccurrence} to the given {@link StringBuilder}.
	 *
	 * @param sb - the {@link StringBuilder} to append to
	 * @param bt - the {@link BusinessTransaction} the bt belongs to
	 * @param occurrence - the {@link BtOccurrence} which is represented by the added line
	 */
	abstract void appendOccurrence(StringBuilder sb, BusinessTransaction bt, BtOccurrence occurrence);


	/**
	 * 
	 * @return the {@link BusinessTransaction.Type} a subclass of this class is intended to be used for.
	 */
	abstract BusinessTransaction.Type getBtType();
	

	/**
	 * Escapes <code>; = ,<code> using <code>\</code> as delimiter char. <code>\n</code> is escaped as <code>\\n</code> rather
	 * than <code>\\\n</code> as Hive first reads the data and then does the escaping.
	 * @param string - the {@link String} to be escaped
	 * @return
	 */
	char[] escape(String string) {
		char[] chars = string.toCharArray();
		int delimCnt = 0;
		for (char c : chars) {
			if (c == FIELD_DELIMITER || c == COLLECTION_DELIMITER || c == MAP_KEY_DELIMITER || c == LINE_DELIMITER) {
				delimCnt++;
			}
		}
		if (delimCnt == 0) {
			return chars;
		}
		char[] result = new char[chars.length + delimCnt];
		int pos = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == LINE_DELIMITER) {
				result[pos++] = ESCAPE_CHAR;
				result[pos++] = LINE_DELIMITER_ESCAPED;
			} else {
				if (c == FIELD_DELIMITER || c == COLLECTION_DELIMITER || c == MAP_KEY_DELIMITER) {
					result[pos++] = ESCAPE_CHAR;
				}
				result[pos++] = chars[i];
			}
		}
		return result;
	}

	
	/**
	 * Appends the given date to the given stringBuilder.
	 * Depending on the configuration it will be appended as a number like "123456789.00" or 
	 * as a JDBC compliant {@link String} like "2013-02-15 10:33:03.226".
	 *
	 * @param stringBuilder
	 * @param date
	 * @see Timestamp
	 */
	void appendDate(StringBuilder stringBuilder, long date) {
		if (numericDate) {
			stringBuilder.append(date);
			stringBuilder.insert(stringBuilder.length() - 3, '.'); //insert a decimal point to make seconds out of the milliseconds
		} else {
			stringBuilder.append(new Timestamp(date));
		}
	}

}