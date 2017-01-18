package zipkin.collector.eventhub;

import com.microsoft.azure.eventhubs.EventData;
import com.microsoft.azure.eventprocessorhost.CloseReason;
import com.microsoft.azure.eventprocessorhost.IEventProcessor;
import com.microsoft.azure.eventprocessorhost.PartitionContext;
import zipkin.Codec;
import zipkin.collector.Collector;

import java.util.Collections;

import static zipkin.storage.Callback.NOOP;

public class ZipkinEventProcessor implements IEventProcessor {

    int checkpointBatchingCount = 0;
    EventHubCollector.Builder config;
    Collector collector;

    public ZipkinEventProcessor(EventHubCollector.Builder builder){
        config = builder;
        collector = config.delegate.build();
    }

    @Override
    public void onOpen(PartitionContext context) throws Exception {
        System.out.println("Opened " + context.getConsumerGroupName());
    }

    @Override
    public void onClose(PartitionContext context, CloseReason reason) throws Exception {
        System.out.println("Closed due to " + reason);
    }

    @Override
    public void onEvents(PartitionContext context, Iterable<EventData> messages) throws Exception {
        for (EventData data : messages)
        {
            byte[] bytes = data.getBody();
            if (bytes[0] == '[') {
                collector.acceptSpans(bytes, Codec.JSON, NOOP);
            }
            else {
                if (bytes[0] == 12 /* TType.STRUCT */) {
                    collector.acceptSpans(bytes, Codec.THRIFT, NOOP);
                } else {
                    collector.acceptSpans(Collections.singletonList(bytes), Codec.THRIFT, NOOP);
                }
            }

            this.checkpointBatchingCount++;
            if ((checkpointBatchingCount % config.checkpointBatchSize) == 0)
            {
                System.out.println("Partition " + context.getPartitionId() + " checkpointing at " +
                        data.getSystemProperties().getOffset() + "," + data.getSystemProperties().getSequenceNumber());
                context.checkpoint(data);
            }
        }
    }

    @Override
    public void onError(PartitionContext context, Throwable error) {
        error.printStackTrace();
    }
}
