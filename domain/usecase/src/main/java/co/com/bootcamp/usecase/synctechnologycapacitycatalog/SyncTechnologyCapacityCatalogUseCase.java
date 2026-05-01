package co.com.bootcamp.usecase.synctechnologycapacitycatalog;

import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class SyncTechnologyCapacityCatalogUseCase implements SyncTechnologyCapacityCatalogService {
    private final TechnologyCapacityCatalogRepository technologyRepository;

    @Override
    public Flux<TechnologyCapacityCatalog> saveAll(Long capacityCatalogId, List<TechnologyCapacityCatalog> technologies) {
        return technologyRepository.deleteByCapacityCatalogId(capacityCatalogId)
                .thenMany(technologyRepository.saveAll(Flux.fromIterable(technologies)));
    }
}
