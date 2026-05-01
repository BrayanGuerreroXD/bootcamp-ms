package co.com.bootcamp.usecase.synccapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SyncCapacityCatalogService {
    Mono<CapacityCatalog> syncCapacityCatalog(CapacityCatalog capacityCatalog);
    Mono<CapacityCatalog> saveCapacityCatalogWithTechnologies(CapacityCatalog capacityCatalog);
    Mono<List<CapacityCatalog>> getAllCapacityCatalogs();
}