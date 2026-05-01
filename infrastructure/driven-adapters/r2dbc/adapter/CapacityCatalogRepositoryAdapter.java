package co.com.bootcamp.drivenAdapters.r2dbc.adapter;

import co.com.bootcamp.drivenAdapters.r2dbc.mapper.CapacityCatalogEntityMapper;
import co.com.bootcamp.drivenAdapters.r2dbc.repository.CapacityCatalogEntityRepository;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CapacityCatalogRepositoryAdapter implements CapacityCatalogRepository {
    private final CapacityCatalogEntityRepository entityRepository;
    private final CapacityCatalogEntityMapper mapper;

    @Override
    public Mono<CapacityCatalog> save(CapacityCatalog capacityCatalog) {
        return entityRepository.save(mapper.toEntity(capacityCatalog))
                .map(mapper::toModel);
    }

    @Override
    public Mono<CapacityCatalog> findByExternalId(Long externalId) {
        return entityRepository.findByExternalId(externalId)
                .map(mapper::toModel);
    }

    @Override
    public Flux<CapacityCatalog> findAll() {
        return entityRepository.findAll()
                .map(mapper::toModel);
    }

    @Override
    public Mono<CapacityCatalog> findById(Long id) {
        return entityRepository.findById(id)
                .map(mapper::toModel);
    }
}