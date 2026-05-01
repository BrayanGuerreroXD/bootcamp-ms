package co.com.bootcamp.drivenadapters.r2dbc.repository;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampCapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampCapacityEntityRepository extends ReactiveCrudRepository<BootcampCapacityEntity, Long> {
    Flux<BootcampCapacityEntity> findByBootcampId(Long bootcampId);
    Flux<BootcampCapacityEntity> findByCapacityId(Long capacityId);
    Mono<Void> deleteByBootcampId(Long bootcampId);
    Mono<Long> countByCapacityId(Long capacityId);

    @Query("""
        SELECT bc.capacity_id FROM bootcamp_capacities bc
        WHERE bc.bootcamp_id != :bootcampId
        AND bc.capacity_id IN (SELECT bc2.capacity_id FROM bootcamp_capacities bc2 WHERE bc2.bootcamp_id = :bootcampId)
        GROUP BY bc.capacity_id
        HAVING COUNT(DISTINCT bc.bootcamp_id) = 1
        """)
    Flux<Long> findCapacityIdsUsedOnlyByBootcamp(Long bootcampId);
}