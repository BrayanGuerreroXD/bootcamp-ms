package co.com.bootcamp.drivenadapters.r2dbc.repository;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampEntityRepository extends ReactiveCrudRepository<BootcampEntity, Long> {
    Flux<BootcampEntity> findAllByOrderByNameAsc();
    Flux<BootcampEntity> findAllByOrderByNameDesc();

    @Query("SELECT COUNT(bc.id) FROM bootcamp_capacities bc WHERE bc.capacity_id = :capacityId")
    Mono<Long> countByCapacityId(Long capacityId);
}