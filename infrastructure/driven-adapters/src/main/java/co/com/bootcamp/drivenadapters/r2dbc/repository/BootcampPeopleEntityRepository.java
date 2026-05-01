package co.com.bootcamp.drivenadapters.r2dbc.repository;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampPeopleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampPeopleEntityRepository extends ReactiveCrudRepository<BootcampPeopleEntity, Long> {
    Flux<BootcampPeopleEntity> findByEmail(String email);
    Mono<Long> countByEmail(String email);
    Mono<Boolean> existsByBootcampIdAndEmail(Long bootcampId, String email);
    Mono<Void> deleteByBootcampId(Long bootcampId);
}