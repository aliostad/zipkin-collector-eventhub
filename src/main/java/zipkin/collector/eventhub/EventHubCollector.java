/**
 * Created by aliostad on 14/01/2017.
 */
package zipkin.collector.eventhub;


import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.IEventProcessorFactory;
import com.microsoft.azure.eventprocessorhost.PartitionContext;
import zipkin.Component;
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
        builder = builder;
        host = new EventProcessorHost(builder.processorHostName,
                builder.eventHubName, builder.consumerGroupName,
                builder.eventHubConnectionString, builder.storageConnectionString,
                builder.storageContainerName, builder.storageBlobPrefix);
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
        String storageContainerName;
        int checkpointBatchSize = 10;

        String processorHostName = UUID.randomUUID().toString();
        String storageBlobPrefix = processorHostName;


        public Builder(){

        }

        public CollectorComponent.Builder eventHubName(String name) {
            eventHubName = name;
            return this;
        }

        public CollectorComponent.Builder consumerGroupName(String name) {
            consumerGroupName = name;
            return this;
        }

        public CollectorComponent.Builder checkpointBatchSize(int size) {
            checkpointBatchSize = size;
            return this;
        }

        public CollectorComponent.Builder eventHubConnectionString(String connectionString) {
            eventHubConnectionString = connectionString;
            return this;
        }

        public CollectorComponent.Builder storageConnectionString(String connectionString) {
            storageConnectionString = connectionString;
            return this;
        }

        public CollectorComponent.Builder storageContainerName(String containerName) {
            storageContainerName = containerName;
            return this;
        }

        public CollectorComponent.Builder storageBlobPrefix(String blobPrefix) {
            storageBlobPrefix = blobPrefix;
            return this;
        }

        public CollectorComponent.Builder processorHostName(String nameForThisProcessorHost) {
            processorHostName = nameForThisProcessorHost;
            return this;
        }

        public CollectorComponent.Builder storage(StorageComponent storage) {
            delegate.storage(storage);
            return this;
        }

        public CollectorComponent.Builder metrics(CollectorMetrics metrics) {
            delegate.metrics(metrics);
            return this;
        }

        public CollectorComponent.Builder sampler(CollectorSampler sampler) {
            delegate.sampler(sampler);
            return this;
        }

        public CollectorComponent build() {
            return new EventHubCollector(this);
        }
    }

    public CollectorComponent start() {
        try {
            host.registerEventProcessorFactory(
                    context -> new ZipkinEventProcess(builder)
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
            host.unregisterEventProcessor();
        } catch (Exception e) {
            e.printStackTrace();
            lastException = e;
        }

        started = false;
    }
}
