package com.dynatrace.diagnostics.btexport.flume;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import org.apache.flume.Context;
import org.apache.flume.conf.Configurable;
import org.apache.flume.serialization.EventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class BtSerializer implements EventSerializer, Configurable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String CONFIG_CHARSET = "charset";
    public static final String CONFIG_DATE_NUMERIC = "numericDate";

    static final char DEFAULT_FIELD_DELIMITER = ';';
    protected char FIELD_DELIMITER = DEFAULT_FIELD_DELIMITER;

    protected static final char MAP_KEY_DELIMITER = '=';

    static final char DEFAULT_COLLECTION_DELIMITER = ',';
    protected char COLLECTION_DELIMITER = DEFAULT_COLLECTION_DELIMITER;

    static final char LINE_DELIMITER = '\n';

    static final char LINE_DELIMITER_ESCAPED = 'n';

    static final char ESCAPE_CHAR = '\\';

    private final OutputStream out;

    private OutputStreamWriter outWriter;

    private boolean numericDate = false;

    public BtSerializer(OutputStream out) {
	this.out = out;
    }

    @Override
    public void configure(Context context) {
	numericDate = context.getBoolean(CONFIG_DATE_NUMERIC, false);
	String charset = context.getString(CONFIG_CHARSET, Charset.defaultCharset().name());
	try {
	    outWriter = new OutputStreamWriter(out, charset);
	    if (log.isInfoEnabled()) {
		StringBuilder sb = new StringBuilder(80);
		sb.append(this.getClass().getSimpleName()).append(": Configured serializer with encoding: '").append(charset).append('\'');
		log.info(sb.toString());
	    }
	} catch (UnsupportedEncodingException uee) {
	    StringBuilder sb = new StringBuilder(80);
	    sb.append(this.getClass().getSimpleName()).append(": Unsupported encoding, disabling serializer: '").append(charset)
		    .append('\'');
	    log.error(sb.toString(), uee);
	}
	String del = context.getString("delimiter");
	if (del == null || del.length() > 1)
	    FIELD_DELIMITER = DEFAULT_FIELD_DELIMITER;
	else
	    FIELD_DELIMITER = del.charAt(0);

	del = context.getString("collection-delimiter");
	if (del == null || del.length() > 1)
	    COLLECTION_DELIMITER = DEFAULT_COLLECTION_DELIMITER;
	else
	    COLLECTION_DELIMITER = del.charAt(0);
	log.info("Field Delimiter: " + FIELD_DELIMITER);
	log.info("Collection Delimiter: " + COLLECTION_DELIMITER);
    }

    @Override
    public void afterCreate() throws IOException {
	// noop
    }

    @Override
    public void afterReopen() throws IOException {
	// noop
    }

    @Override
    public void beforeClose() throws IOException {
	outWriter = null;
    }

    @Override
    public void flush() throws IOException {
	if (outWriter != null ) {
	    outWriter.flush();
	}
    }

    @Override
    public boolean supportsReopen() {
	return false;
    }

    /**
     * Writes the given {@link String} to the outputstream of this
     * {@link EventSerializer}.
     * 
     * @param data
     * @throws IOException
     */
    protected void write(String data) throws IOException {
	if (outWriter != null) {
	    outWriter.write(data);
	}
    }

    /**
     * Escapes <code>; = ,<code> using <code>\</code> as delimiter char.
     * <code>\n</code> is escaped as <code>\\n</code> rather than
     * <code>\\\n</code> as Hive first reads the data and then does the
     * escaping.
     * 
     * @param string
     *            - the {@link String} to be escaped
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
     * Appends the given date to the given stringBuilder. Depending on the
     * configuration it will be appended as a number like "123456789.00" or as a
     * JDBC compliant {@link String} like "2013-02-15 10:33:03.226".
     * 
     * @param stringBuilder
     * @param date
     * @see Timestamp
     */
    void appendDate(StringBuilder stringBuilder, long date) {
	if (numericDate) {
	    stringBuilder.append(date);
	    stringBuilder.insert(stringBuilder.length() - 3, '.'); // insert a
								   // decimal
								   // point to
								   // make
								   // seconds
								   // out of the
								   // milliseconds
	} else {
	    stringBuilder.append(new Timestamp(date));
	}
    }

}
