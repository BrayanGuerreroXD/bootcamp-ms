package co.com.bootcamp.model.capacitycatalog.gateways;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityCatalogRepository {
    Mono<CapacityCatalog> save(CapacityCatalog capacityCatalog);
    Mono<CapacityCatalog> findByExternalId(Long externalId);
    Flux<CapacityCatalog> findAll();
    Mono<CapacityCatalog> findById(Long id);
}
