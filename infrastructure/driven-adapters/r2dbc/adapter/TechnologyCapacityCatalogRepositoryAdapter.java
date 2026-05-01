package co.com.bootcamp.drivenAdapters.r2dbc.adapter;

import co.com.bootcamp.drivenAdapters.r2dbc.mapper.TechnologyCapacityCatalogEntityMapper;
import co.com.bootcamp.drivenAdapters.r2dbc.repository.TechnologyCapacityCatalogEntityRepository;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TechnologyCapacityCatalogRepositoryAdapter implements TechnologyCapacityCatalogRepository {
    private final TechnologyCapacityCatalogEntityRepository entityRepository;
    private final TechnologyCapacityCatalogEntityMapper mapper;

    @Override
    public Mono<TechnologyCapacityCatalog> save(TechnologyCapacityCatalog technologyCapacityCatalog) {
        return entityRepository.save(mapper.toEntity(technologyCapacityCatalog))
                .map(mapper::toModel);
    }

    @Override
    public Flux<TechnologyCapacityCatalog> saveAll(Flux<TechnologyCapacityCatalog> technologies) {
        return entityRepository.saveAll(technologies.map(mapper::toEntity))
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteByCapacityCatalogId(Long capacityCatalogId) {
        return entityRepository.deleteByCapacityCatalogId(capacityCatalogId);
    }

    @Override
    public Flux<TechnologyCapacityCatalog> findByCapacityCatalogId(Long capacityCatalogId) {
        return entityRepository.findByCapacityCatalogId(capacityCatalogId)
                .map(mapper::toModel);
    }

    @Override
    public Flux<TechnologyCapacityCatalog> findAll() {
        return entityRepository.findAll()
                .map(mapper::toModel);
    }
}