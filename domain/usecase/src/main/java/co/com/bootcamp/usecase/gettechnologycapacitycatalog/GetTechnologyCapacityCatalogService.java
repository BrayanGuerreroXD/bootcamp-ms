package co.com.bootcamp.usecase.gettechnologycapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetTechnologyCapacityCatalogService {
    Mono<CapacityCatalog> getAllByCapacityId(Long capacityId);
    Flux<TechnologyCapacityCatalog> getAllTechnologies();
}