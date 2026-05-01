package co.com.bootcamp.model.event.gateways;

import co.com.bootcamp.model.event.BootcampCapacityMatchEvent;
import co.com.bootcamp.model.event.BootcampDeleteMatchEvent;
import reactor.core.publisher.Mono;

public interface EventGateway {
    Mono<Void> publishBootcampDeleteMatch(BootcampDeleteMatchEvent event);
    Mono<Void> publishBootcampCapacityMatch(BootcampCapacityMatchEvent event);
}