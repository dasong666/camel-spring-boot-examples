#!/bin/bash

AMQUSER=test
AMQPASWD=catdog123
AMQHOST=192.168.0.18:8161

#broker metrics
curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/TotalMessageCount | jq '.request.attribute,.value'

curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/ConnectionCount | jq '.request.attribute,.value'

curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/java.lang:type=Threading/PeakThreadCount | jq '.request.attribute,.value' 

#curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\",component=addresses,address=\"JB440web\",subcomponent=queues,routing-type=\"multicast\",queue=\"JB440Queue\"/MessageCount

#curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/AddressMemoryUsagePercentage

curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/DiskStoreUsage | jq '.request.attribute,.value'

#JVM metrics
curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/java.lang:type=Memory/HeapMemoryUsage | jq '.request.attribute,.value'

#curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/java.lang:type=Memory/HeapMemoryUsage 

curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/java.lang:type=OperatingSystem/ProcessCpuLoad | jq '.request.attribute,.value'

#curl -L -H "Origin: http://localhost" -u $AMQUSER:$AMQPASWD http://$AMQHOST/console/jolokia/read/java.lang:type=GarbageCollector,name=Copy/LastGcInfo
