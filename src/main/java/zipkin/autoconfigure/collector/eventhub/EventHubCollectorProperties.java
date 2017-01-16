package zipkin.autoconfigure.collector.eventhub;

import org.springframework.boot.context.properties.ConfigurationProperties;
import zipkin.collector.eventhub.EventHubCollector;


/**
 * Created by aliostad on 16/01/2017.
 */
@ConfigurationProperties("zipkin.collector.eventhub")
public class EventHubCollectorProperties {

    private String eventHubName = "zipkin";
    private String consumerGroupName = "$Default";
    private String eventHubConnectionString;
    private String storageConnectionString;
    private String storageContainerName;
    private int checkpointBatchSize = 10;

    private String processorHostName = "";
    private String storageBlobPrefix = getProcessorHostName();

    public String getEventHubName() {
        return eventHubName;
    }

    public void setEventHubName(String eventHubName) {
        this.eventHubName = eventHubName;
    }

    public String getConsumerGroupName() {
        return consumerGroupName;
    }

    public void setConsumerGroupName(String consumerGroupName) {
        this.consumerGroupName = consumerGroupName;
    }

    public String getEventHubConnectionString() {
        return eventHubConnectionString;
    }

    public void setEventHubConnectionString(String eventHubConnectionString) {
        this.eventHubConnectionString = eventHubConnectionString;
    }

    public String getStorageConnectionString() {
        return storageConnectionString;
    }

    public void setStorageConnectionString(String storageConnectionString) {
        this.storageConnectionString = storageConnectionString;
    }

    public String getStorageContainerName() {
        return storageContainerName;
    }

    public void setStorageContainerName(String storageContainerName) {
        this.storageContainerName = storageContainerName;
    }

    public int getCheckpointBatchSize() {
        return checkpointBatchSize;
    }

    public void setCheckpointBatchSize(int checkpointBatchSize) {
        this.checkpointBatchSize = checkpointBatchSize;
    }

    public String getProcessorHostName() {
        return processorHostName;
    }

    public void setProcessorHostName(String processorHostName) {
        this.processorHostName = processorHostName;
    }

    public String getStorageBlobPrefix() {
        return storageBlobPrefix;
    }

    public void setStorageBlobPrefix(String storageBlobPrefix) {
        this.storageBlobPrefix = storageBlobPrefix;
    }

    public EventHubCollector.Builder toBuilder() {
        EventHubCollector.builder()
                .
    }
}
