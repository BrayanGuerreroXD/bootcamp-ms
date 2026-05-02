package co.com.bootcamp.model.event.gateways;

import co.com.bootcamp.model.event.BootcampEvent;
import co.com.bootcamp.model.event.CapacityBootcampSyncEvent;
import co.com.bootcamp.model.event.BootcampDeleteMatchEvent;
import reactor.core.publisher.Mono;

public interface EventGateway {
    Mono<Void> publishBootcampDeleteMatch(BootcampDeleteMatchEvent event);
    Mono<Void> publishCapacitiesBootcampsMatch(CapacityBootcampSyncEvent event);
    Mono<Void> publishBootcampReportSync(BootcampEvent event);
}