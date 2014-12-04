
FLUME_HOME=./apache-flume-1.3.1-bin
FLUME_CONF=flume-conf.properties
mkdir `grep directory $FLUME_CONF | sed "s#.*directory = \(.*\)#\1#g"`
$FLUME_HOME/bin/flume-ng agent -C dtFlume.jar -n agent1 -c $FLUME_HOME/conf -f $FLUME_CONF -Dflume.root.logger=INFO,console
