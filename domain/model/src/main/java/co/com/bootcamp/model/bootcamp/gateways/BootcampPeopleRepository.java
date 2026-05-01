package co.com.bootcamp.model.bootcamp.gateways;

import co.com.bootcamp.model.bootcamp.BootcampPeople;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampPeopleRepository {
    Mono<BootcampPeople> save(BootcampPeople bootcampPeople);
    Flux<BootcampPeople> findByEmail(String email);
    Mono<Long> countByEmail(String email);
    Mono<Boolean> existsByBootcampIdAndEmail(Long bootcampId, String email);
    Mono<Void> deleteByBootcampId(Long bootcampId);
}