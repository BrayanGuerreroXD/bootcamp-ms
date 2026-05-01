package co.com.bootcamp.drivenadapters.r2dbc.repository;

import co.com.bootcamp.drivenadapters.r2dbc.entity.TechnologyCapacityCatalogEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TechnologyCapacityCatalogEntityRepository extends ReactiveCrudRepository<TechnologyCapacityCatalogEntity, Long> {
    Mono<Void> deleteByCapacityCatalogId(Long capacityCatalogId);
    Flux<TechnologyCapacityCatalogEntity> findByCapacityCatalogId(Long capacityCatalogId);
}