package co.com.bootcamp.model.technologycapacitycatalog.gateways;

import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TechnologyCapacityCatalogRepository {
    Mono<TechnologyCapacityCatalog> save(TechnologyCapacityCatalog technologyCapacityCatalog);
    Flux<TechnologyCapacityCatalog> saveAll(Flux<TechnologyCapacityCatalog> technologies);
    Mono<Void> deleteByCapacityCatalogId(Long capacityCatalogId);
    Flux<TechnologyCapacityCatalog> findByCapacityCatalogId(Long capacityCatalogId);
    Flux<TechnologyCapacityCatalog> findAll();
}
