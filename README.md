# Big Data Business Transaction Bridge

The Big Data Business Transaction Bridge makes it possible to store Business Transaction Results to HDFS, Files, Databases and other stores easily. The results are either written as CSV or JSON files
and can be included in Hive as external tables.

Flume is a distributed service for collecting, aggregating, and moving large amounts of log data. In this case we describe how it can be used to receive data from the dynaTrace [Real Time Business
Transactions Feed](https://community/display/DOCDT55/Real+Time+Business+Transactions+Feed) via HTTP Post requests and store it in a Hadoop HDFS or other stores.

![images_community/download/attachments/103907934/logo.png](images_community/download/attachments/103907934/logo.png)

## Library Details

|Name | Big Data Business Transaction Bridge
| :--- | :---
|Version|1.0.1
|Flume-Version|0.9.4, 1.3.1
|dynaTrace Version|5.5
|Author|Peter Kaiser / Michael Kopp
|License|[dynaTrace BSD](dynaTraceBSD.txt)
|Support|[Not Supported ](https://community.compuwareapm.com/community/display/DL/Support+Levels#SupportLevels-Community)
|Contents|[Big Data Business Transaction Bridge for Flume 0.9.4](dtFlume094.jar)
||[Big Data Business Transaction Bridge for Flume 1.3.1](dtFlume.jar)  
||[Google protobuf 2.4.1](protobuf-2.4.1.jar)  
||[Windows Flume Start Script](runFlume.bat) 
||[Linux Shell Flume Script](runFlume.sh)  
||[Flume CSV File Configuration](SampleFileSetup-conf.properties)  
||[Flume JSON File Configuration](flume-json-conf.properties)  
||[Flume HDFS Configuration](SampleHDFSSetup-conf.properties)

  * [Prerequisites](#prerequisites)

  * [Flume File based Setup with Flume 1.3.1](#flume-file-based-setup-with-flume-1.3.1)

    * [Alternative Setup - JSON instead of CSV](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-AlternativeSetup-JSONinsteadofCSV)

  * [Hadoop Flume Setup](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-HadoopFlumeSetup)

  * [Hive Setup](#hive-setup)

    * [Query data via Hive](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-QuerydataviaHive)

      * [Try Hive Locally](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-TryHiveLocally)

  * [Appendix](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Appendix)

    * [Flume 0.9.4](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Flume0.9.4)

## Prerequisites

The Big Data Business Transaction Bridge has been developed and tested using Flume 0.9.4 and Flume 1.3.1. Flume can be used to send, route and store the data anywhere. Flume is both configurable and
extendable but already contains a lot of out of the box channels (ways to send data) and sinks (places to store data).

This page will focus on two areas of storage. In the first section will deal with the flume setup and describe how to use it to store the Business Transaction data in CSV or JSON files. It will also
explain how we can route different types of Business Transactions into different files/directories.

The second section will explain how to store the produced files in Hadoop, setup external Hive tables for access and query the data via hive. Basic installation of Hadoop, Hive and Flume beyond a
simple local setup is not covered here. See  
<http://hadoop.apache.org>, <https://cwiki.apache.org/confluence/display/Hive/GettingStarted> and <http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html> for more information.

  * See [Real Time Business Transactions Feed](https://community/display/DOCDT55/Real+Time+Business+Transactions+Feed) for a description of how to configure dynaTrace to send business transaction data. 

  * The description of the setup of the Big Data Business Transaction Bridge is split into two parts: 

    * How to use flume to store the consumed data in CSV and JSON files. 

    * How to setup Hadoop for storage and Hive for querying of the data. 

## Flume File based Setup with Flume 1.3.1

The supported Flume versions are 0.9.4 and 1.3.1. You should use Flume 1.3.1 whenever you can. The 0.9.4 version should only be used if you have an older version of Flume and cannot use the newer
Flume Version. Check the [Appendix](Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Flume0.9.4) for Flume 0.9.4 setup.

For Flume 1.3.1 the Big Data Business Transaction Bridge consists of three CSV and one JSON serializer to store the Business Transaction results.

Flume 1.3.1 already comes with protobuf so you do not need to add it. Flume has to be started with the classpath pointing to 'dtFlume.jar', a configuration file that contains the source, channels and
sinks and the name of the agent to execute. The simplest way to do this is to create a shell script like this:

    
    
    FLUME_HOME=./apache-flume-1.3.1-bin
    $FLUME_HOME/bin/flume-ng agent -C dtFlume.jar -n agent1 -c $FLUME_HOME/conf \
    -f flume-conf.properties -Dflume.root.logger=INFO,console

You can also just download the attached [runFlume.sh](runFlume.sh) which will also attempt to create the data directories found in the flume configuration. For Windows it will look a little different,
you can use the attached [runFlume.bat](runFlume.bat) as a starting point.

This will start flume as an agent, add the _dtFlume.jar_ to the classpath, execute the _agent1_ as defined in the given _flume-conf.properties_ configuration file

An example config, which connects a Http Source three File Sinks writing to individual directories looks as follows (also see [sample attachment](SampleFileSetup-conf.properties)):

    
    
    # Name the components on this agent
    agent1.sources = HTTPSource
    agent1.sinks = PurePathSink UserActionSink VisitSink
    agent1.channels = PurePathChannel UserActionChannel VisitChannel
    # Describe/configure HTTPSource
    agent1.sources.HTTPSource.type = org.apache.flume.source.http.HTTPSource
    agent1.sources.HTTPSource.port = 4321
    agent1.sources.HTTPSource.handler = com.dynatrace.diagnostics.btexport.flume.BtExportHandler
    # Describe sinks
    agent1.sinks.PurePathSink.type = com.dynatrace.diagnostics.flume.RollingFileSink
    # once every 10 min
    agent1.sinks.PurePathSink.sink.rollInterval = 600
    # Force cutoff at 10 MB
    agent1.sinks.PurePathSink.sink.rollSize = 10
    agent1.sinks.PurePathSink.sink.directory = data/pp
    agent1.sinks.PurePathSink.sink.batchSize = 1000
    # Attention the key for the serialize on file_roll is a little different
    agent1.sinks.PurePathSink.sink.serializer = com.dynatrace.diagnostics.btexport.flume.BtPurePathSerializerBuilder
    # Describe sinks
    agent1.sinks.UserActionSink.type = com.dynatrace.diagnostics.flume.RollingFileSink
    agent1.sinks.UserActionSink.sink.rollInterval = 600
    agent1.sinks.UserActionSink.sink.rollSize = 10
    agent1.sinks.UserActionSink.sink.directory = data/pa
    agent1.sinks.UserActionSink.sink.batchSize = 1000
    # Attention the key for the serialize on file_roll is a little different
    agent1.sinks.UserActionSink.sink.serializer = com.dynatrace.diagnostics.btexport.flume.BtPageActionSerializerBuilder
    # Describe sinks
    agent1.sinks.VisitSink.type = com.dynatrace.diagnostics.flume.RollingFileSink
    agent1.sinks.VisitSink.sink.rollInterval = 600
    agent1.sinks.VisitSink.sink.rollSize = 10
    agent1.sinks.VisitSink.sink.directory = data/visit
    agent1.sinks.VisitSink.sink.batchSize = 1000
    # Attention the key for the serialize on file_roll is a little different
    agent1.sinks.VisitSink.sink.serializer = com.dynatrace.diagnostics.btexport.flume.BtVisitSerializerBuilder
    # Use a channel which buffers events in memory
    agent1.channels.PurePathChannel.type = memory
    agent1.channels.PurePathChannel.capacity = 1000
    agent1.channels.PurePathChannel.transactionCapactiy = 100
    agent1.channels.UserActionChannel.type = memory
    agent1.channels.UserActionChannel.capacity = 1000
    agent1.channels.UserActionChannel.transactionCapactiy = 100
    agent1.channels.VisitChannel.type = memory
    agent1.channels.VisitChannel.capacity = 1000
    agent1.channels.VisitChannel.transactionCapactiy = 100
    # Bind the source and sink to the channel
    agent1.sources.HTTPSource.channels = PurePathChannel UserActionChannel VisitChannel
    agent1.sinks.PurePathSink.channel = PurePathChannel
    agent1.sinks.UserActionSink.channel = UserActionChannel
    agent1.sinks.VisitSink.channel = VisitChannel
    agent1.sources.HTTPSource.selector.type = multiplexing
    agent1.sources.HTTPSource.selector.header = btType
    agent1.sources.HTTPSource.selector.mapping.PUREPATH = PurePathChannel
    agent1.sources.HTTPSource.selector.mapping.PAGE_ACTION = UserActionChannel
    agent1.sources.HTTPSource.selector.mapping.VISIT = VisitChannel
    agent1.sources.HTTPSource.selector.default = PurePathChannel

The Bridge also contains a special File Sink that extends the default file_roll sink with the ability to define a maximum file size and removes empty files.

The File Sink does not create the directories it writes to. If those directories do not exist the setup will fail and no data will be written!

The BtSerializers additionally allow to specify a character encoding different from the platform's standard. Any character encoding supported by java is possible:

    
    
    agent1.sinks.VisitSink.serializer.charset = ISO-8859-2

The Serializers also have two parameters that can be used to configure alternative delimiters if the default values are not desired.

    
    
    #CSV Field delimiter
    agent1.sinks.VisitSink.sink.serializer.delimiter=|
    #CSV Array field delimiter
    agent1.sinks.VisitSink.sink.serializer.collection-delimiter=;

### Alternative Setup - JSON instead of CSV

You can also export Business Transaction data to JSON instead of CSV

    
    
    agent1.sinks.VisitSink.sink.serializer = com.dynatrace.diagnostics.btexport.flume.BtExportJSONSerializerBuilder

**Sample of exported data (JSON)**
    
    
    { apdex : "1.0",
    application : "easyTravel portal",
    converted : false,
    dimensions : {[-]
    Client Family of Visits : "Desktop Browser",
    Client IP Address of Visit : "170.225.221.40",
    Client Type of Visit : "HTC",
    Client of Visits : "Firefox 16.0",
    Connection Type of Visits : "Broadband (>1500 kb/s)",
    Country of Visits : "United States - Arizona",
    Operating System of Visits : "Windows 7",
    User Experience of Visits : "satisfied"
    },
    endTime : "2013-04-19 08:14:29.871+0000",
    measures : {[-]
    Action Count : "20.0",
    Bounce Rate : "0.0",
    Failed Actions Count : "0.0"
    },
    name : "Detailed Visit Data",
    startTime : "2013-04-19 08:14:25.150+0000",
    type : "VISIT",
    visitId : 102020
    }

## Hadoop Flume Setup

The Basic setup describes the steps necessary to prepare the Hadoop/Hive environment for the Big Data Business Transaction Bridge, but does not describe how to setup Hadoop/Hive itself. Please refer
to <http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html> for a basic setup of Hadoop.

The are three different types of Business Transactions, depending on what they are based on:

  * PurePath 

  * PageAction 

  * Visit 

As these types contain different data, three directories have to be created in HDFS.

    
    
    $HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/pp
    $HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/pa
    $HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/visit
    $HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/pp
    $HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/pa
    $HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/visit

Adjust the directory names according to your needs.

In order to let flume write to Hadoop you will need to modify the flume configuration to use the HDFS sink instead of the file sink (also see [sample attachment](SampleHDFSSetup-conf.properties)).

    
    
    # Name the components on this agent
    agent1.sources = HTTPSource
    agent1.sinks = PurePathSink UserActionSink VisitSink NullSink
    agent1.channels = PurePathChannel UserActionChannel VisitChannel NullChannel
    # Describe/configure HTTPSource
    agent1.sources.HTTPSource.type = org.apache.flume.source.http.HTTPSource
    agent1.sources.HTTPSource.port = 4321
    agent1.sources.HTTPSource.handler = com.dynatrace.diagnostics.btexport.flume.BtExportHandler
    # Describe sinks
    agent1.sinks.PurePathSink.type = hdfs
    agent1.sinks.PurePathSink.hdfs.path = hdfs://localhost:9000/user/bts/pp
    agent1.sinks.PurePathSink.hdfs.fileType = DataStream
    agent1.sinks.PurePathSink.hdfs.filePrefix = export
    agent1.sinks.PurePathSink.hdfs.fileSuffix = .txt
    agent1.sinks.PurePathSink.hdfs.rollInterval = 120
    agent1.sinks.PurePathSink.hdfs.rollSize = 131072
    agent1.sinks.PurePathSink.serializer = com.dynatrace.diagnostics.btexport.flume.BtPurePathSerializerBuilder
    agent1.sinks.UserActionSink.type = hdfs
    agent1.sinks.UserActionSink.hdfs.path = hdfs://localhost:9000/user/bts/pa
    agent1.sinks.UserActionSink.hdfs.fileType = DataStream
    agent1.sinks.UserActionSink.hdfs.filePrefix = export
    agent1.sinks.UserActionSink.hdfs.fileSuffix = .txt
    agent1.sinks.UserActionSink.hdfs.rollInterval = 120
    agent1.sinks.UserActionSink.hdfs.rollSize = 131072
    agent1.sinks.UserActionSink.serializer = com.dynatrace.diagnostics.btexport.flume.BtPageActionSerializerBuilder
    agent1.sinks.VisitSink.type = hdfs
    agent1.sinks.VisitSink.hdfs.path = hdfs://localhost:9000/user/bts/visit
    agent1.sinks.VisitSink.hdfs.fileType = DataStream
    agent1.sinks.VisitSink.hdfs.filePrefix = export
    agent1.sinks.VisitSink.hdfs.fileSuffix = .txt
    agent1.sinks.VisitSink.hdfs.rollInterval = 120
    agent1.sinks.VisitSink.hdfs.rollSize = 131072
    agent1.sinks.VisitSink.serializer = com.dynatrace.diagnostics.btexport.flume.BtVisitSerializerBuilder
    agent1.sinks.NullSink.type = null
    # Use a channel which buffers events in memory
    agent1.channels.PurePathChannel.type = memory
    agent1.channels.PurePathChannel.capacity = 1000
    agent1.channels.PurePathChannel.transactionCapactiy = 100
    agent1.channels.UserActionChannel.type = memory
    agent1.channels.UserActionChannel.capacity = 1000
    agent1.channels.UserActionChannel.transactionCapactiy = 100
    agent1.channels.VisitChannel.type = memory
    agent1.channels.VisitChannel.capacity = 1000
    agent1.channels.VisitChannel.transactionCapactiy = 100
    # Bind the source and sink to the channel
    agent1.sources.HTTPSource.channels = PurePathChannel UserActionChannel VisitChannel NullChannel
    agent1.sinks.PurePathSink.channel = PurePathChannel
    agent1.sinks.UserActionSink.channel = UserActionChannel
    agent1.sinks.VisitSink.channel = VisitChannel
    agent1.sinks.NullSink.channel = NullChannel
    agent1.sources.HTTPSource.selector.type = multiplexing
    agent1.sources.HTTPSource.selector.header = btType
    agent1.sources.HTTPSource.selector.mapping.PUREPATH = PurePathChannel
    agent1.sources.HTTPSource.selector.mapping.PAGE_ACTION = UserActionChannel
    agent1.sources.HTTPSource.selector.mapping.VISIT = VisitChannel
    agent1.sources.HTTPSource.selector.default = NullChannel

Where as the FileSink has additional _sink_ level when defining the serializer the hdfs sink omits this level!

Adjust the directory names according to your HDFS setup. When you start flume with this configuration files the result data will be stored your hadoop cluster.

## Hive Setup

Hive is query engine that can execute against file data stored locally or in your hadoop cluster. In order for Hive to access the stored data you will need to use the CSV Serializers and create
external tables:

    
    
    create external table BT_PP (
    Name STRING,
    Application STRING,
    PathId STRING,
    StartTime TIMESTAMP,
    Dimensions MAP<STRING, STRING>,
    Values MAP<STRING, DOUBLE>,
    Failed BOOLEAN,
    VisitId BIGINT,
    ResponseTime DOUBLE,
    Duration DOUBLE,
    CpuTime DOUBLE,
    ExecTime DOUBLE,
    SuspensionTime DOUBLE,
    SyncTime DOUBLE,
    WaitTime DOUBLE
    )
    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\;' ESCAPED BY '\\' COLLECTION ITEMS TERMINATED BY ',' MAP KEYS TERMINATED BY '='
    LOCATION '/user/bts/pp';
    create external table BT_PA (
    Name STRING,
    Application STRING,
    PathId STRING,
    StartTime TIMESTAMP,
    Dimensions MAP<STRING, STRING>,
    Values MAP<STRING, DOUBLE>,
    Failed BOOLEAN,
    ActionName STRING,
    Url STRING,
    VisitId BIGINT,
    ResponseTime DOUBLE,
    Duration DOUBLE,
    CpuTime DOUBLE,
    ExecTime DOUBLE,
    SuspensionTime DOUBLE,
    SyncTime DOUBLE,
    WaitTime DOUBLE
    )
    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\;' ESCAPED BY '\\' COLLECTION ITEMS TERMINATED BY ',' MAP KEYS TERMINATED BY '='
    LOCATION '/user/bts/pa';
    create external table BT_VISIT (
    Name STRING,
    Application STRING,
    VisitId BIGINT,
    StartTime TIMESTAMP,
    EndTime TIMESTAMP,
    Dimensions MAP<STRING, STRING>,
    Values MAP<STRING, DOUBLE>,
    User STRING,
    Converted BOOLEAN,
    Apdex DOUBLE
    )
    ROW FORMAT DELIMITED FIELDS TERMINATED BY '\;' ESCAPED BY '\\' COLLECTION ITEMS TERMINATED BY ',' MAP KEYS TERMINATED BY '='
    LOCATION '/user/bts/visit';

You can also run the command below using this file : [bigdatabtbridge_table_creation.sql](bigdatabtbridge_table_creation.sql)

    
    
    hive --service cli -f bigdatabtbridge_table_creation.sql

### Query data via Hive

Hive is very sql-like. To query data in a fully setup hadoop/hive environment simply start the hive command line interface via the _hive_ command. You will get a prompt and can execute commands:

    
    
    hive> show tables;
    OK
    bt_pa
    bt_pp
    bt_visit
    Time taken: 11.495 seconds
    hive>

To select all visit data from today simply execute

    
    
    select * from BT_VISIT where
    datediff(from_unixtime(unix_timestamp()), startTime) < 2;

If you want to get all user actions for a specific user you can execute the following

    
    
    select pa.* from BT_PA pa join BT_VISIT v on (pa.visitId = v.visitId) where v.user="myname"

#### Try Hive Locally

in order to try out Hive locally without a full Hadoop setup you should do the following:

  * setup Flume for Files, NOT HDFS 

  * download the Hadoop and Hive Binaries and extract them into a directory 

  * setup Hive for [local only mode](https://cwiki.apache.org/Hive/gettingstarted.html#GettingStarted-Hive%252CMapReduceandLocalMode)

  * start the hive shell 

  * setup external tables against the flume directories you configured 

  * execute your queries 

## Appendix

### Flume 0.9.4

For Flume 0.9.4 the Big Data Business Transaction Bridge consists of an HttpSource, accepting Http POST requests on a configurable port, and a decorator for each of the three Business Transaction
types.

  * The environment variable 'FLUME_CLASSPATH' should include 'dtFlume094.jar' and 'protobuf-2.4.1.jar'. 

  * In $FLUME_HOME/conf/flume-conf.xml the value for "flume.collector.output.format" has to be set to "raw", as the formatting will be handled by the provided factory. 

  * In $FLUME_HOME/conf/flume-site.xml the following section should be added: 
    
        <property>
    <name>flume.plugin.classes</name>
    <value>com.dynatrace.diagnostics.flume.HttpSource,com.dynatrace.diagnostics.flume.btexport.BtPurePathDecorator,com.dynatrace.diagnostics.flume.btexport.BtPageActionDecorator,com.dynatrace.diagnostics.flume.btexport.BtVisitDecorator</value>
    <description>Comma separated list of plugin classes</description>
    </property>

  * Assuming there are the two flume nodes 'agent1' and 'collector1', these can be configured via the Flume masters web-interface as follows: 
    
        agent1: httpSource(4321) | agentSink("localhost", 54321);
    collector1: collectorSource(54321) | [{btPurePathDecorator() => collectorSink("hdfs://localhost:9000/user/bts/pp/", "btPurePath", 120000)},
    {btPageActionDecorator() => collectorSink("hdfs://localhost:9000/user/bts/pa/", "btPageAction", 120000)},
    {btVisitDecorator() => collectorSink("hdfs://localhost:9000/user/bts/visit/", "btVisit", 120000)}];

  * By default dates are encoded as JDBC compatible strings. It's possible to output the dates as Unix-timestamp: 
    
        btPurePathDecorator("true")

  * The generated files are encoded using the platforms default character encoding. However it's possible to specify any encoding supported by Java: 
    
        btPurePathDecorator("false", "UTF-8")

In this case dates will be written as JDBC compatible strings. The encoding will be UTF-8.

