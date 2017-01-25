/**
 * Created by aliostad on 14/01/2017.
 */
package zipkin.collector.eventhub;


import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import zipkin.collector.*;
import zipkin.storage.StorageComponent;

import java.io.IOException;
import java.util.UUID;


public class EventHubCollector implements CollectorComponent {

    private Builder builder;
    private EventProcessorHost host;
    private Boolean started = false;
    private Exception lastException;

    public EventHubCollector(Builder builder){
        this.builder = builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder implements CollectorComponent.Builder {
        Collector.Builder delegate = Collector.builder(EventHubCollector.class);
        CollectorMetrics metrics = CollectorMetrics.NOOP_METRICS;
        String eventHubName = "zipkin";
        String consumerGroupName = "$Default";
        String eventHubConnectionString;
        String storageConnectionString;
        String storageContainerName = "zipkin";
        int checkpointBatchSize = 10;

        String processorHostName = UUID.randomUUID().toString();
        String storageBlobPrefix = processorHostName;


        public Builder(){

        }

        public EventHubCollector.Builder eventHubName(String name) {
            eventHubName = name;
            return this;
        }

        public EventHubCollector.Builder consumerGroupName(String name) {
            consumerGroupName = name;
            return this;
        }

        public EventHubCollector.Builder checkpointBatchSize(int size) {
            checkpointBatchSize = size;
            return this;
        }

        public EventHubCollector.Builder eventHubConnectionString(String connectionString) {
            eventHubConnectionString = connectionString;
            return this;
        }

        public EventHubCollector.Builder storageConnectionString(String connectionString) {
            storageConnectionString = connectionString;
            return this;
        }

        public EventHubCollector.Builder storageContainerName(String containerName) {
            storageContainerName = containerName;
            return this;
        }

        public EventHubCollector.Builder storageBlobPrefix(String blobPrefix) {
            storageBlobPrefix = blobPrefix;
            return this;
        }

        public EventHubCollector.Builder processorHostName(String nameForThisProcessorHost) {
            processorHostName = nameForThisProcessorHost;
            return this;
        }

        public EventHubCollector.Builder storage(StorageComponent storage) {
            delegate.storage(storage);
            return this;
        }

        public EventHubCollector.Builder metrics(CollectorMetrics metrics) {
            delegate.metrics(metrics);
            return this;
        }

        public EventHubCollector.Builder sampler(CollectorSampler sampler) {
            delegate.sampler(sampler);
            return this;
        }

        public EventHubCollector build() {
            return new EventHubCollector(this);
        }
    }

    public String getEventHubName() {
        return builder.eventHubName;
    }

    public String getConsumerGroupName() {
        return builder.consumerGroupName;
    }

    public String getEventHubConnectionString() {
        return builder.eventHubConnectionString;
    }

    public String getStorageConnectionString() {
        return builder.storageConnectionString;
    }

    public String getStorageContainerName() {
        return builder.storageContainerName;
    }

    public int getCheckpointBatchSize() {
        return builder.checkpointBatchSize;
    }

    public String getProcessorHostName() {
        return builder.processorHostName;
    }

    public String getStorageBlobPrefix() {
        return builder.storageBlobPrefix;
    }

    public CollectorComponent start() {
        try {
            host = new EventProcessorHost(builder.processorHostName,
                    builder.eventHubName, builder.consumerGroupName,
                    builder.eventHubConnectionString, builder.storageConnectionString,
                    builder.storageContainerName, builder.storageBlobPrefix);

            host.registerEventProcessorFactory(
                    context -> new ZipkinEventProcessor(builder)
            );
            started = true;
        } catch (Exception e) {
            e.printStackTrace();
            lastException = e;
        }
        return this;
    }

    public CheckResult check() {
        return started || lastException==null ? CheckResult.OK : CheckResult.failed(lastException);
    }

    public void close() throws IOException {
        try {
            if(host != null) {
                host.unregisterEventProcessor();
            }
        } catch (Exception e) {
            e.printStackTrace();
            lastException = e;
        }

        started = false;
    }
}
