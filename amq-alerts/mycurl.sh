#!/bin/bash

#broker metrics
curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/TotalMessageCount

curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/ConnectionCount

curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/java.lang:type=Threading/PeakThreadCount

curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\",component=addresses,address=\"JB440web\",subcomponent=queues,routing-type=\"multicast\",queue=\"JB440Queue\"/MessageCount

#curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/AddressMemoryUsagePercentage

#curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/org.apache.activemq.artemis:broker=\"0.0.0.0\"/DiskStoreUsage

#JVM metrics
#curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/java.lang:type=Memory/HeapMemoryUsage

#curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/java.lang:type=OperatingSystem/ProcessCpuLoad

#curl -L -H "Origin: http://localhost" -u test:catdog123 http://192.168.0.18:8161/console/jolokia/read/java.lang:type=GarbageCollector,name=Copy/LastGcInfo
