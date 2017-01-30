package zipkin.autoconfigure.collector.eventhub;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import zipkin.collector.CollectorMetrics;
import zipkin.collector.CollectorSampler;
import zipkin.collector.eventhub.EventHubCollector;
import zipkin.storage.StorageComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by aliostad on 16/01/2017.
 */
@Configuration
@EnableConfigurationProperties(EventHubCollectorProperties.class)
@Conditional(EventHubSetCondition.class)
public class EventHubCollectorAutoConfiguration {

  private static final Logger logger = Logger.getLogger(EventHubCollectorAutoConfiguration.class.getName());


  @Bean
  EventHubCollector eventHubCollector(EventHubCollectorProperties properties,
                                      CollectorSampler sampler,
                                      CollectorMetrics metrics,
                                      StorageComponent storage) {


    logger.log(Level.INFO,"===========EventHubCollectorAutoConfiguration==============");

    return properties.toBuilder()
        .sampler(sampler)
        .storage(storage)
        .metrics(metrics)
        .build()
        .start();
  }
}

