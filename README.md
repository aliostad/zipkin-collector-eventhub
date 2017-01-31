# zipkin-collector-eventhub
Zipkin Collector for Azure EventHub

## What is zipkin-collector-eventhub?
It is a Zipkin collecor that can collect spans from Azure EventHub, instead of Kafka. [Azure EventHub](https://azure.microsoft.com/en-us/services/event-hubs/) is an Azure PaaS Service has a functionality (and design) very similar to Apache Kafka where on one hand, the data is be pushed to a sink. On the other hand, the data is read by the consumers from partitioned storage where only a single consumer reads from a partition guaranteeing ordering of the messages. On regular intervals, partition gets checkpointed.

**zipkin-collector-eventhub** allows Azure EventHub to be used to collect Zipkin spans from systems.

## Getting started
Currently the jar is not available on the Maven repository (coming soon) but is equally easy to get the source and build it. To get started:

### 1- Clone the source and build
``` bash
mkdir zipkin-collector-eventhub
cd zipkin-collector-eventhub
git clone git@github.com:aliostad/zipkin-collector-eventhub.git
mvn package
```
If you do not have maven, get maven [here](http://maven.apache.org/install.html).

### 2- Unpackage MODULE jar into an empty folder
copy zipkin-collector-eventhub-autoconfig-x.x.x-SNAPSHOT-module.jar (that has been package in the `target` folder) into an empty folder and unpackage
``` bash
jar xf zipkin-collector-eventhub-autoconfig-0.1.0-SNAPSHOT-module.jar
```
You may then delete the jar itself.

### 3- Download zipkin-server jar
Download the latest zipkin-server jar (which is named zipkin.jar) from [here](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec). For more information visit [zipkin-server homepage](https://github.com/openzipkin/zipkin/tree/master/zipkin-server).  

### 4- create an `application.properties` file for configuration next to the zipkin.jar file
Populate the configuration - make sure the resources (Azure Storage, EventHub, etc) exist. **Only storageConnectionString is mandatory** the rest are optional and must be used only to override the defaults:
```
zipkin.collector.eventhub.storageConnectionString=<azure storage connection string>
zipkin.collector.eventhub.eventHubName=<name of the eventhub, default is zipkin>
zipkin.collector.eventhub.consumerGroupName=<name of the consumer group, default is $Default>
zipkin.collector.eventhub.storageContainerName=<name of the storage container, default is zipkin>
zipkin.collector.eventhub.processorHostName=<name of the processor host, default is a randomly generated GUID>
zipkin.collector.eventhub.storageBlobPrefix=<the path within container where blobs are created for partition lease, processorHostName>
```

### 5- Run the server along with the collector
Assuming `zipkin.jar` and `application.properties` are in the current working directory
```
java -Dloader.path=/where/jar/was/unpackaged -cp zipkin.jar org.springframework.boot.loader.PropertiesLauncher --spring.config.location=application.properties --zipkin.collector.eventhub.eventHubConnectionString="<eventhub connection string, make sure quoted otherwise won't work>"
```
