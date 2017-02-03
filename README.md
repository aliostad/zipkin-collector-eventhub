# zipkin-collector-eventhub
Zipkin Collector for Azure EventHub

## What is zipkin-collector-eventhub?
It is a Zipkin collecor that can collect spans from Azure EventHub, instead of Kafka. [Azure EventHub](https://azure.microsoft.com/en-us/services/event-hubs/) is an Azure PaaS Service has a functionality (and design) very similar to Apache Kafka where on one hand, the data is be pushed to a sink. On the other hand, the data is read by the consumers from partitioned storage where only a single consumer reads from a partition guaranteeing ordering of the messages. On regular intervals, partition gets checkpointed.

**zipkin-collector-eventhub** allows Azure EventHub to be used to collect Zipkin spans from systems.

## Project has moved to [OpenZipkin org](https://github.com/openzipkin/zipkin-azure)!

