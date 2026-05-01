package co.com.bootcamp.usecase.getcapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import reactor.core.publisher.Flux;

public interface GetCapacityCatalogService {
    Flux<CapacityCatalog> getAll();
}