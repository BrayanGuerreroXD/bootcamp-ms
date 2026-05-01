package co.com.bootcamp.usecase.synccapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class SyncCapacityCatalogUseCase implements SyncCapacityCatalogService {
    private final CapacityCatalogRepository capacityCatalogRepository;
    private final TechnologyCapacityCatalogRepository technologyRepository;

    @Override
    public Mono<CapacityCatalog> syncCapacityCatalog(CapacityCatalog capacityCatalog) {
        return capacityCatalogRepository.findByExternalId(capacityCatalog.getExternalId())
                .flatMap(existing -> {
                    CapacityCatalog updated = existing.toBuilder()
                            .name(capacityCatalog.getName())
                            .build();
                    return capacityCatalogRepository.save(updated);
                })
                .switchIfEmpty(Mono.defer(() -> capacityCatalogRepository.save(capacityCatalog)));
    }

    @Override
    public Mono<CapacityCatalog> saveCapacityCatalogWithTechnologies(CapacityCatalog capacityCatalog) {
        return syncCapacityCatalog(capacityCatalog)
                .flatMap(savedCatalog -> {
                    List<TechnologyCapacityCatalog> technologies = capacityCatalog.getTechnologies();
                    if (technologies == null || technologies.isEmpty()) {
                        return Mono.just(savedCatalog);
                    }
                    List<TechnologyCapacityCatalog> technologiesWithCatalogId = technologies.stream()
                            .map(tech -> tech.toBuilder()
                                    .capacityCatalogId(savedCatalog.getId())
                                    .build())
                            .toList();
                    return technologyRepository.deleteByCapacityCatalogId(savedCatalog.getId())
                            .thenMany(technologyRepository.saveAll(technologiesWithCatalogId))
                            .collectList()
                            .thenReturn(savedCatalog);
                });
    }

    @Override
    public Mono<List<CapacityCatalog>> getAllCapacityCatalogs() {
        return capacityCatalogRepository.findAll()
                .collectList();
    }
}
