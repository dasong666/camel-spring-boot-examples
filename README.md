# Simple AMQ Metrics Alerting - Design Overview
- Set of core ActiveMQ metrics as Camel Routes
- Each route is wrapped in a configurable timer
- Parsing JSON response
- Sending collected metrics as JSON payload to external webhook
- Deployable framework using spring-boot-camel
- Sample of ActiveMQ Metrics in this demo
TotalMessageCount
ConnectionCount
PeakThreadCount
HeapMemoryUsage
DiskStoreUsage
ProcessCpuLoad

# Future Enhancement Ideas
- Compare each metrics response to alert threshold defined in ```application.properties```
- Trigger customized alert
- Define happy path and alternative flows (error-handling)
- Use Prometheus ActiveMQ plugin to collect metrics
- Setup Grafana dashboard using Prometheus metrics data source

# ActiveMQ Artemis Monitoring API's
- REST style Jolokia API endpoints
- TotalMessageCount example:
```shell
http://{broker-host}:8161/console/jolokia/read/org.apache.activemq.artemis:broker="0.0.0.0"/TotalMessageCount
```

# Installation
- Clone this repository
- A sample shell script is included in the home directory of repo ```amq-alerts/mycurl.sh```
- Note: the script is pointing to a local instance of ActiveMQ/Artemis.  Please update the according to your own environment.
- Edit ```src/main/resources/application.properties``` with following changes according to your specific environment
- Update the property ```management.endpoints.config``` to point to the absolute path containing ```mycurl.sh```
- For example: ```management.endpoints.config = /Users/foobar/amq-alerts/mycurl.sh```
- Update the property ```application.webhook.endpoint``` to point to your own instance of Webhook/alert receiver
- For example: ```application.webhook.endpoint = https://{your site}```
- From the ```amq-alerts``` subdirectory containing your ```pom.xml``` run the following Maven command to start the application
```shell
mvn spring-boot:run
```
