package zipkin.autoconfigure.collector.eventhub;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by aliostad on 16/01/2017.
 */
public final class EventHubSetCondition extends SpringBootCondition {
  static final String PROPERTY_NAME = "zipkin.collector.eventhub.eventHubConnectionString";

  public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata a) {

    String eventHubProperty = context.getEnvironment().getProperty(PROPERTY_NAME);
    ConditionOutcome outcome = eventHubProperty == null || eventHubProperty.isEmpty() ?
        ConditionOutcome.noMatch(PROPERTY_NAME + " isn't set") :
        ConditionOutcome.match();

    return outcome;
  }

}
