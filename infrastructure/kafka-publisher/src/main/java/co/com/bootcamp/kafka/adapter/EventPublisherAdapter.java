package co.com.bootcamp.kafka.adapter;

import co.com.bootcamp.kafka.config.KafkaTopicsProperties;
import co.com.bootcamp.model.event.BootcampCapacityMatchEvent;
import co.com.bootcamp.model.event.BootcampDeleteMatchEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventGateway {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    @Override
    public Mono<Void> publishBootcampDeleteMatch(BootcampDeleteMatchEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.getDeleteBootcampMatch(), event)).then();
    }

    @Override
    public Mono<Void> publishBootcampCapacityMatch(BootcampCapacityMatchEvent event) {
        return Mono.fromFuture(kafkaTemplate.send(topics.getSyncBootcampCapacityMatch(), event)).then();
    }
}