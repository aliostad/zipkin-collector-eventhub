package zipkin.collector.eventhub;

import com.microsoft.azure.eventprocessorhost.EventProcessorHost;
import com.microsoft.azure.eventprocessorhost.PartitionContext;
import org.junit.Test;
import zipkin.collector.InMemoryCollectorMetrics;
import zipkin.storage.InMemoryStorage;

/**
 * Created by aliostad on 18/01/2017.
 */
public class ZipkinEventProcessorTests {

    @Test
    public void canCreateZipkinEventProcessor(){
        ZipkinEventProcessor zipkinEventProcessor = new ZipkinEventProcessor(getBuilder());
    }

    private EventHubCollector.Builder getBuilder(){
        return EventHubCollector.builder()
                .storage(new InMemoryStorage());

    }

}
