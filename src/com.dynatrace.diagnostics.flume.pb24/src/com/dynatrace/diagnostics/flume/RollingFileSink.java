package com.dynatrace.diagnostics.flume;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.flume.Context;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.formatter.output.PathManager;

public class RollingFileSink extends org.apache.flume.sink.RollingFileSink {

    private final static Logger log = Logger.getLogger(RollingFileSink.class.getName());

    private final PathManager pathManager;

    private int rollSize = 0;

    private Field shouldRotate;

    public RollingFileSink() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
	super();
	Field field = org.apache.flume.sink.RollingFileSink.class.getDeclaredField("pathController");
	field.setAccessible(true);
	pathManager = (PathManager) field.get(this);
	shouldRotate = org.apache.flume.sink.RollingFileSink.class.getDeclaredField("shouldRotate");
	shouldRotate.setAccessible(true);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void configure(Context context) {
	// TODO Auto-generated method stub
	String wrongSerializer = context.getString("serializer");
	if (wrongSerializer != null)
	{
	    log.warning("Serializer should be under \"sink.serializer\" but is \"serializer\". Fixing...");
	    context.put("sink.serializer",wrongSerializer);
	}
	    
	Map<String,String> serializerSub = context.getSubProperties("serializer.");
	if (serializerSub != null && !serializerSub.isEmpty())
	{
	    log.warning("Serializer should be under \"sink.serializer\" but is \"serializer\". Fixing...");
	    for (Map.Entry<String, String> me : serializerSub.entrySet())
	    {
		context.put("sink.serializer." + me.getKey(), me.getValue());
	    }
	}
	if (context.getString("sink.serializer") == null)
	    log.warning("No Serializer was configured, data is most likely not readable!!");
	if (log.isLoggable(Level.INFO)) {
	    log.info("Configuration: " + context.getParameters());
	}
	super.configure(context);
	Integer rollSize = context.getInteger("sink.rollSize");
	if (rollSize != null && rollSize != 0) {
	    this.rollSize = rollSize * 1024 * 1024;
	}

    }

    @Override
    public void start() {
	// TODO Auto-generated method stub
	super.start();
    }

    @Override
    public Status process() throws EventDeliveryException {
	File oldFile = pathManager.getCurrentFile();
	Status process = super.process();
	try {
	    File newFile = pathManager.getCurrentFile();
	    if (!oldFile.equals(newFile)) {
		if (oldFile.length() == 0)
		    oldFile.delete();
	    } else if (rollSize != 0 && oldFile.length() > rollSize)
		shouldRotate.setBoolean(this, true); // force rotate due to size

	} catch (Exception e) {
	    log.log(Level.WARNING, "Could not delete potential empty file", e);
	}
	return process;
    }

    @Override
    public void stop() {
	File oldFile = pathManager.getCurrentFile();
	super.stop();
	if (oldFile != null && oldFile.exists() && oldFile.length() == 0)
	    oldFile.delete();

    }

}
