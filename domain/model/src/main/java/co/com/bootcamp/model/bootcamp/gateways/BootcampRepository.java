package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampRepository {
    Mono<Bootcamp> save(Bootcamp bootcamp);
    Mono<Bootcamp> findById(Long id);
    Flux<Bootcamp> findAll();
    Flux<Bootcamp> findAllOrderByName(OrderDirection direction);
    Mono<Void> deleteById(Long id);
    Mono<Long> countByCapacityId(Long capacityId);
}