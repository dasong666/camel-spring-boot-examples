# AMQ-Alerts

- Using Camel Libraries
- Have a camel context that can scrap broker JMX metrics and have them processed.
- Alert rules defined in ```application.properties```  something like: rule name, condition, time interval
- When that rule fires, then it will send a message to another Camel Route
- This camel route will agnostically grab the alert message and send to an Email recipiant
- The email configuraiton is also in the ```application.properties```

# Design Overview
- Set of core metrics as Camel Routes
- Each route is wrapped in a configurable timer
- Parsing JSON response
- Compare response to alert threshold in ```application.properties```
- Positive trigger email alert via Camel SMTP route
- #TODO#: define happy path and alternative (error-handling)

# ActiveMQ Artemis Monitoring API's
- REST style Jolokia API endpoints
- TotalMessageCount example:
```shell
http://{broker-host}:8161/console/jolokia/read/org.apache.activemq.artemis:broker="0.0.0.0"/TotalMessageCount
```

# Installation
- Clone this repository
- A sample shell script is included in the home directory of repo ```mycurl.sh```
- Note: the script is pointing to a local instance of ActiveMQ/Artemis.  Please update the according to your own environment.
- Edit ```src/main/resources/application.properties```
and update the property ```management.endpoints.config``` to point to the absolute path containing ```mycurl.sh```
- For example: ```/Users/foobar/amq-alerts/mycurl.sh```
- From the directory containing your ```pom.xml``` run the following Maven command to start the application
```shell
mvn spring-boot:run
```
