package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampCapacityRepository {
    Mono<BootcampCapacity> save(BootcampCapacity bootcampCapacity);
    Flux<BootcampCapacity> saveAll(Flux<BootcampCapacity> capacities);
    Flux<BootcampCapacity> findByBootcampId(Long bootcampId);
    Flux<BootcampCapacity> findByCapacityId(Long capacityId);
    Mono<Void> deleteByBootcampId(Long bootcampId);
    Mono<Long> countByCapacityId(Long capacityId);
    Flux<Long> findCapacityIdsUsedOnlyByBootcamp(Long bootcampId);
}