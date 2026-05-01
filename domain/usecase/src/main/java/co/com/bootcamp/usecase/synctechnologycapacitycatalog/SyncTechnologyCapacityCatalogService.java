package co.com.bootcamp.usecase.synctechnologycapacitycatalog;

import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import reactor.core.publisher.Flux;

import java.util.List;

public interface SyncTechnologyCapacityCatalogService {
    Flux<TechnologyCapacityCatalog> saveAll(Long capacityCatalogId, List<TechnologyCapacityCatalog> technologies);
}