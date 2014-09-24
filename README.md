<head>
<title>Big Data Business Transaction Bridge</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<meta content="Scroll Wiki Publisher" name="generator"/>
<link type="text/css" rel="stylesheet" href="css/blueprint/liquid.css" media="screen, projection"/>
<link type="text/css" rel="stylesheet" href="css/blueprint/print.css" media="print"/>
<!--[if lt IE 8]><link rel="stylesheet" href="css/blueprint/ie.css" type="text/css" media="screen, projection"/><![endif]-->
<link type="text/css" rel="stylesheet" href="css/content-style.css" media="screen, projection, print"/>
<link type="text/css" rel="stylesheet" href="css/screen.css" media="screen, projection"/>
<link type="text/css" rel="stylesheet" href="css/print.css" media="print"/>
</head>
<body>
<div class="container" style="min-width: 760px;">
<div class="header block">
<div class="header-left column span-6">
</div>
<div class="column span-18 header-right last">
<h4>Big Data Business Transaction Bridge</h4>
</div>
</div>
<div class="block">
<div class="toc column span-6 prepend-top">
<h3>Table of Contents
</h3>
<ul class="toc">
</ul>
</div>
<div id="103907934" class="content column span-18 last">
<h1>Big Data Business Transaction Bridge</h1>
<p>
The Big Data Business Transaction Bridge makes it possible to store Business Transaction Results to HDFS, Files, Databases and other stores easily. The results are either written as CSV or JSON files and can be included in Hive as external tables. </p>
<p>
Flume is a distributed service for collecting, aggregating, and moving large amounts of log data. In this case we describe how it can be used to receive data from the dynaTrace <a href="https://community/display/DOCDT55/Real+Time+Business+Transactions+Feed">Real Time Business Transactions Feed</a> via HTTP Post requests and store it in a Hadoop HDFS or other stores. </p>
<p>
<img src="images_community/download/attachments/103907934/logo.png" alt="images_community/download/attachments/103907934/logo.png" class="confluence-embedded-image" />
</p>
<p>
</p>
<p>
</p>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-LibraryDetails" >
<h2>Library Details</h2>
<div class="tablewrap">
<table>
<thead class=" "></thead><tfoot class=" "></tfoot><tbody class=" "> <tr>
<td rowspan="1" colspan="1">
<p>
Name </p>
</td>
<td rowspan="1" colspan="1">
<p>
<strong class=" ">Big Data Business Transaction Bridge</strong> </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
Version </p>
</td>
<td rowspan="1" colspan="1">
<p>
1.0.1 </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
Flume-Version </p>
</td>
<td rowspan="1" colspan="1">
<p>
0.9.4, 1.3.1 </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
dynaTrace Version </p>
</td>
<td rowspan="1" colspan="1">
<p>
5.5 </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
Author </p>
</td>
<td rowspan="1" colspan="1">
<p>
Peter Kaiser / Michael Kopp </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
License </p>
</td>
<td rowspan="1" colspan="1">
<p>
<a href="attachments_5275722_2_dynaTraceBSD.txt">dynaTrace BSD</a> </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
Support </p>
</td>
<td rowspan="1" colspan="1">
<p>
<a href="https://community/display/DL/Support+Levels#SupportLevels-Community">Not Supported </a> </p>
</td>
</tr>
<tr>
<td rowspan="1" colspan="1">
<p>
Contents </p>
</td>
<td rowspan="1" colspan="1">
<p>
<a href="attachments_109608968_3_dtFlume094.jar">Big Data Business Transaction Bridge for Flume 0.9.4</a><br/><a href="attachments_109608967_3_dtFlume.jar">Big Data Business Transaction Bridge for Flume 1.3.1</a><br/><a href="attachments_109608969_1_protobuf-2.4.1.jar">Google protobuf 2.4.1</a><br/><a href="attachments_122978307_1_runFlume.bat">Windows Flume Start Script<br/></a><a href="attachments_130220061_2_runFlume.sh">Linux Shell Flume Script</a><br/><a href="attachments_120913928_1_SampleFileSetup-conf.properties">Flume CSV File Configuration</a><br/><a href="attachments_130220062_1_flume-json-conf.properties">Flume JSON File Configuration</a><br/><a href="attachments_120913927_1_SampleHDFSSetup-conf.properties">Flume HDFS Configuration</a> </p>
</td>
</tr>
</tbody> </table>
</div>
<ul class="toc-indentation "><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Prerequisites">Prerequisites</a> </p>
</li><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-FlumeFilebasedSetupwithFlume1.3.1">Flume File based Setup with Flume 1.3.1</a> </p>
<ul class="toc-indentation "><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-AlternativeSetup-JSONinsteadofCSV">Alternative Setup - JSON instead of CSV</a> </p>
</li></ul></li><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-HadoopFlumeSetup">Hadoop Flume Setup</a> </p>
</li><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-HiveSetup">Hive Setup</a> </p>
<ul class="toc-indentation "><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-QuerydataviaHive">Query data via Hive</a> </p>
<ul class="toc-indentation "><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-TryHiveLocally">Try Hive Locally</a> </p>
</li></ul></li></ul></li><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Appendix">Appendix</a> </p>
<ul class="toc-indentation "><li class=" "> <p>
<a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Flume0.9.4">Flume 0.9.4</a> </p>
</li></ul></li></ul> </div>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-Prerequisites" >
<h2>Prerequisites</h2>
<p>
The Big Data Business Transaction Bridge has been developed and tested using Flume 0.9.4 and Flume 1.3.1. Flume can be used to send, route and store the data anywhere. Flume is both configurable and extendable but already contains a lot of out of the box channels (ways to send data) and sinks (places to store data). </p>
<p>
This page will focus on two areas of storage. In the first section will deal with the flume setup and describe how to use it to store the Business Transaction data in CSV or JSON files. It will also explain how we can route different types of Business Transactions into different files/directories. </p>
<p>
The second section will explain how to store the produced files in Hadoop, setup external Hive tables for access and query the data via hive. Basic installation of Hadoop, Hive and Flume beyond a simple local setup is not covered here. See<br/><a href="http://hadoop.apache.org">http://hadoop.apache.org</a>, <a href="https://cwiki.apache.org/confluence/display/Hive/GettingStarted">https://cwiki.apache.org/confluence/display/Hive/GettingStarted</a> and <a href="http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html">http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html</a> for more information. </p>
<ul class=" "><li class=" "> <p>
See <a href="https://community/display/DOCDT55/Real+Time+Business+Transactions+Feed">Real Time Business Transactions Feed</a> for a description of how to configure dynaTrace to send business transaction data. </p>
</li><li class=" "> <p>
The description of the setup of the Big Data Business Transaction Bridge is split into two parts: </p>
<ul class=" "><li class=" "> <p>
How to use flume to store the consumed data in CSV and JSON files. </p>
</li><li class=" "> <p>
How to setup Hadoop for storage and Hive for querying of the data. </p>
</li></ul></li></ul> </div>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-FlumeFilebasedSetupwithFlume1.3.1" >
<h2>Flume File based Setup with Flume 1.3.1</h2>
<p>
The supported Flume versions are 0.9.4 and 1.3.1. You should use Flume 1.3.1 whenever you can. The 0.9.4 version should only be used if you have an older version of Flume and cannot use the newer Flume Version. Check the <a href="Big_Data_Business_Transaction_Bridge.html#103907934_BigDataBusinessTransactionBridge-Flume0.9.4">Appendix</a> for Flume 0.9.4 setup. </p>
<p>
For Flume 1.3.1 the Big Data Business Transaction Bridge consists of three CSV and one JSON serializer to store the Business Transaction results. </p>
<p>
Flume 1.3.1 already comes with protobuf so you do not need to add it. Flume has to be started with the classpath pointing to 'dtFlume.jar', a configuration file that contains the source, channels and sinks and the name of the agent to execute. The simplest way to do this is to create a shell script like this: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>FLUME_HOME=./apache-flume-1.3.1-bin
$FLUME_HOME/bin/flume-ng agent -C dtFlume.jar -n agent1 -c $FLUME_HOME/conf \
-f flume-conf.properties -Dflume.root.logger=INFO,console</code></pre>
</div>
</div>
<p>
You can also just download the attached <a href="attachments_130220061_2_runFlume.sh">runFlume.sh</a> which will also attempt to create the data directories found in the flume configuration. For Windows it will look a little different, you can use the attached <a href="attachments_122978307_1_runFlume.bat">runFlume.bat</a> as a starting point. </p>
<p>
This will start flume as an agent, add the <i class=" ">dtFlume.jar</i> to the classpath, execute the <i class=" ">agent1</i> as defined in the given <i class=" ">flume-conf.properties</i> configuration file </p>
<p>
An example config, which connects a Http Source three File Sinks writing to individual directories looks as follows (also see <a href="attachments_120913928_1_SampleFileSetup-conf.properties">sample attachment</a>): </p>
<div class="confbox programlisting">
<div class="content">
<pre><code># Name the components on this agent
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
agent1.sources.HTTPSource.selector.default = PurePathChannel</code></pre>
</div>
</div>
<p>
The Bridge also contains a special File Sink that extends the default file_roll sink with the ability to define a maximum file size and removes empty files. </p>
<div class="confbox admonition admonition-note">
<p>
The File Sink does not create the directories it writes to. If those directories do not exist the setup will fail and no data will be written! </p>
</div>
<p>
The BtSerializers additionally allow to specify a character encoding different from the platform's standard. Any character encoding supported by java is possible: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>agent1.sinks.VisitSink.serializer.charset = ISO-8859-2</code></pre>
</div>
</div>
<p>
The Serializers also have two parameters that can be used to configure alternative delimiters if the default values are not desired. </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>#CSV Field delimiter
agent1.sinks.VisitSink.sink.serializer.delimiter=|
#CSV Array field delimiter
agent1.sinks.VisitSink.sink.serializer.collection-delimiter=;</code></pre>
</div>
</div>
<div class="section-3" id="103907934_BigDataBusinessTransactionBridge-AlternativeSetup-JSONinsteadofCSV" >
<h3>Alternative Setup - JSON instead of CSV</h3>
<p>
You can also export Business Transaction data to JSON instead of CSV </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>agent1.sinks.VisitSink.sink.serializer = com.dynatrace.diagnostics.btexport.flume.BtExportJSONSerializerBuilder</code></pre>
</div>
</div>
<p>
<strong class=" ">Sample of exported data (JSON)</strong> </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>{ apdex : &quot;1.0&quot;,
application : &quot;easyTravel portal&quot;,
converted : false,
dimensions : {[-]
Client Family of Visits : &quot;Desktop Browser&quot;,
Client IP Address of Visit : &quot;170.225.221.40&quot;,
Client Type of Visit : &quot;HTC&quot;,
Client of Visits : &quot;Firefox 16.0&quot;,
Connection Type of Visits : &quot;Broadband (&gt;1500 kb/s)&quot;,
Country of Visits : &quot;United States - Arizona&quot;,
Operating System of Visits : &quot;Windows 7&quot;,
User Experience of Visits : &quot;satisfied&quot;
},
endTime : &quot;2013-04-19 08:14:29.871+0000&quot;,
measures : {[-]
Action Count : &quot;20.0&quot;,
Bounce Rate : &quot;0.0&quot;,
Failed Actions Count : &quot;0.0&quot;
},
name : &quot;Detailed Visit Data&quot;,
startTime : &quot;2013-04-19 08:14:25.150+0000&quot;,
type : &quot;VISIT&quot;,
visitId : 102020
}</code></pre>
</div>
</div>
</div>
</div>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-HadoopFlumeSetup" >
<h2>Hadoop Flume Setup</h2>
<p>
The Basic setup describes the steps necessary to prepare the Hadoop/Hive environment for the Big Data Business Transaction Bridge, but does not describe how to setup Hadoop/Hive itself. Please refer to <a href="http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html">http://hadoop.apache.org/docs/r1.1.2/single_node_setup.html</a> for a basic setup of Hadoop. </p>
<p>
The are three different types of Business Transactions, depending on what they are based on: </p>
<ul class=" "><li class=" "> <p>
PurePath </p>
</li><li class=" "> <p>
PageAction </p>
</li><li class=" "> <p>
Visit </p>
</li></ul> <p>
As these types contain different data, three directories have to be created in HDFS. </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>$HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/pp
$HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/pa
$HADOOP_HOME/bin/hadoop fs -mkdir /user/bts/visit
$HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/pp
$HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/pa
$HADOOP_HOME/bin/hadoop fs -chmod g+w /user/bts/visit</code></pre>
</div>
</div>
<p>
Adjust the directory names according to your needs. </p>
<p>
In order to let flume write to Hadoop you will need to modify the flume configuration to use the HDFS sink instead of the file sink (also see <a href="attachments_120913927_1_SampleHDFSSetup-conf.properties">sample attachment</a>). </p>
<div class="confbox programlisting">
<div class="content">
<pre><code># Name the components on this agent
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
agent1.sources.HTTPSource.selector.default = NullChannel</code></pre>
</div>
</div>
<p>
</p>
<div class="confbox admonition admonition-note">
<p>
Where as the FileSink has additional <i class=" ">sink</i> level when defining the serializer the hdfs sink omits this level! </p>
</div>
<p>
Adjust the directory names according to your HDFS setup. When you start flume with this configuration files the result data will be stored your hadoop cluster. </p>
</div>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-HiveSetup" >
<h2>Hive Setup</h2>
<p>
Hive is query engine that can execute against file data stored locally or in your hadoop cluster. In order for Hive to access the stored data you will need to use the CSV Serializers and create external tables: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>create external table BT_PP (
Name STRING,
Application STRING,
PathId STRING,
StartTime TIMESTAMP,
Dimensions MAP&lt;STRING, STRING&gt;,
Values MAP&lt;STRING, DOUBLE&gt;,
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
Dimensions MAP&lt;STRING, STRING&gt;,
Values MAP&lt;STRING, DOUBLE&gt;,
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
Dimensions MAP&lt;STRING, STRING&gt;,
Values MAP&lt;STRING, DOUBLE&gt;,
User STRING,
Converted BOOLEAN,
Apdex DOUBLE
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\;' ESCAPED BY '\\' COLLECTION ITEMS TERMINATED BY ',' MAP KEYS TERMINATED BY '='
LOCATION '/user/bts/visit';</code></pre>
</div>
</div>
<p>
You can also run the command below using this file : <a href="attachments_118226989_1_bigdatabtbridge_table_creation.sql">bigdatabtbridge_table_creation.sql</a> </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>hive --service cli -f bigdatabtbridge_table_creation.sql</code></pre>
</div>
</div>
<div class="section-3" id="103907934_BigDataBusinessTransactionBridge-QuerydataviaHive" >
<h3>Query data via Hive</h3>
<p>
Hive is very sql-like. To query data in a fully setup hadoop/hive environment simply start the hive command line interface via the <i class=" ">hive</i> command. You will get a prompt and can execute commands: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>hive&gt; show tables;
OK
bt_pa
bt_pp
bt_visit
Time taken: 11.495 seconds
hive&gt;</code></pre>
</div>
</div>
<p>
To select all visit data from today simply execute </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>select * from BT_VISIT where
datediff(from_unixtime(unix_timestamp()), startTime) &lt; 2;</code></pre>
</div>
</div>
<p>
If you want to get all user actions for a specific user you can execute the following </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>select pa.* from BT_PA pa join BT_VISIT v on (pa.visitId = v.visitId) where v.user=&quot;myname&quot;</code></pre>
</div>
</div>
<div class="section-4" id="103907934_BigDataBusinessTransactionBridge-TryHiveLocally" >
<h4>Try Hive Locally</h4>
<p>
in order to try out Hive locally without a full Hadoop setup you should do the following: </p>
<ul class=" "><li class=" "> <p>
setup Flume for Files, NOT HDFS </p>
</li><li class=" "> <p>
download the Hadoop and Hive Binaries and extract them into a directory </p>
</li><li class=" "> <p>
setup Hive for <a href="https://cwiki.apache.org/Hive/gettingstarted.html#GettingStarted-Hive%252CMapReduceandLocalMode">local only mode</a> </p>
</li><li class=" "> <p>
start the hive shell </p>
</li><li class=" "> <p>
setup external tables against the flume directories you configured </p>
</li><li class=" "> <p>
execute your queries </p>
</li></ul> </div>
</div>
</div>
<div class="section-2" id="103907934_BigDataBusinessTransactionBridge-Appendix" >
<h2>Appendix</h2>
<div class="section-3" id="103907934_BigDataBusinessTransactionBridge-Flume0.9.4" >
<h3>Flume 0.9.4</h3>
<p>
For Flume 0.9.4 the Big Data Business Transaction Bridge consists of an HttpSource, accepting Http POST requests on a configurable port, and a decorator for each of the three Business Transaction types. </p>
<ul class=" "><li class=" "> <p>
The environment variable 'FLUME_CLASSPATH' should include 'dtFlume094.jar' and 'protobuf-2.4.1.jar'. </p>
</li><li class=" "> <p>
In $FLUME_HOME/conf/flume-conf.xml the value for &quot;flume.collector.output.format&quot; has to be set to &quot;raw&quot;, as the formatting will be handled by the provided factory. </p>
</li><li class=" "> <p>
In $FLUME_HOME/conf/flume-site.xml the following section should be added: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>&lt;property&gt;
&lt;name&gt;flume.plugin.classes&lt;/name&gt;
&lt;value&gt;com.dynatrace.diagnostics.flume.HttpSource,com.dynatrace.diagnostics.flume.btexport.BtPurePathDecorator,com.dynatrace.diagnostics.flume.btexport.BtPageActionDecorator,com.dynatrace.diagnostics.flume.btexport.BtVisitDecorator&lt;/value&gt;
&lt;description&gt;Comma separated list of plugin classes&lt;/description&gt;
&lt;/property&gt;</code></pre>
</div>
</div>
</li><li class=" "> <p>
Assuming there are the two flume nodes 'agent1' and 'collector1', these can be configured via the Flume masters web-interface as follows: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>agent1: httpSource(4321) | agentSink(&quot;localhost&quot;, 54321);
collector1: collectorSource(54321) | [{btPurePathDecorator() =&gt; collectorSink(&quot;hdfs://localhost:9000/user/bts/pp/&quot;, &quot;btPurePath&quot;, 120000)},
{btPageActionDecorator() =&gt; collectorSink(&quot;hdfs://localhost:9000/user/bts/pa/&quot;, &quot;btPageAction&quot;, 120000)},
{btVisitDecorator() =&gt; collectorSink(&quot;hdfs://localhost:9000/user/bts/visit/&quot;, &quot;btVisit&quot;, 120000)}];</code></pre>
</div>
</div>
</li><li class=" "> <p>
By default dates are encoded as JDBC compatible strings. It's possible to output the dates as Unix-timestamp: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>btPurePathDecorator(&quot;true&quot;)</code></pre>
</div>
</div>
</li><li class=" "> <p>
The generated files are encoded using the platforms default character encoding. However it's possible to specify any encoding supported by Java: </p>
<div class="confbox programlisting">
<div class="content">
<pre><code>btPurePathDecorator(&quot;false&quot;, &quot;UTF-8&quot;)</code></pre>
</div>
</div>
<p>
In this case dates will be written as JDBC compatible strings. The encoding will be UTF-8. </p>
</li></ul> </div>
</div>
</div>
</div>
<div class="footer">
</div>
</div>
</body>
</html>
