package co.com.bootcamp.usecase.gettechnologycapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class GetTechnologyCapacityCatalogUseCase implements GetTechnologyCapacityCatalogService {
    private final CapacityCatalogRepository capacityCatalogRepository;
    private final TechnologyCapacityCatalogRepository technologyRepository;

    @Override
    public Mono<CapacityCatalog> getAllByCapacityId(Long capacityId) {
        return capacityCatalogRepository.findById(capacityId)
                .flatMap(capacity -> technologyRepository.findByCapacityCatalogId(capacityId)
                        .collectList()
                        .map(technologies -> capacity.toBuilder()
                                .technologies(technologies)
                                .build()));
    }

    @Override
    public Flux<TechnologyCapacityCatalog> getAllTechnologies() {
        return technologyRepository.findAll();
    }
}
