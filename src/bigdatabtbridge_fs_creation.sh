export HADOOP=/usr
export BTS_HOME=/user/bts

$HADOOP/bin/hadoop fs -mkdir       /tmp
$HADOOP/bin/hadoop fs -mkdir       /user/hive/warehouse
$HADOOP/bin/hadoop fs -chmod g+w   /tmp
$HADOOP/bin/hadoop fs -chmod g+w   /user/hive/warehouse

$HADOOP/bin/hadoop fs -mkdir $BTS_HOME/pp
$HADOOP/bin/hadoop fs -mkdir $BTS_HOME/pa
$HADOOP/bin/hadoop fs -mkdir $BTS_HOME/visit

$HADOOP/bin/hadoop fs -chmod g+w $BTS_HOME/pp
$HADOOP/bin/hadoop fs -chmod g+w $BTS_HOME/pa
$HADOOP/bin/hadoop fs -chmod g+w $BTS_HOME/visit
