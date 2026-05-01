package co.com.bootcamp.drivenadapters.r2dbc.repository;

import co.com.bootcamp.drivenadapters.r2dbc.entity.CapacityCatalogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CapacityCatalogEntityRepository extends ReactiveCrudRepository<CapacityCatalogEntity, Long> {
    Mono<CapacityCatalogEntity> findByExternalId(Long externalId);
}