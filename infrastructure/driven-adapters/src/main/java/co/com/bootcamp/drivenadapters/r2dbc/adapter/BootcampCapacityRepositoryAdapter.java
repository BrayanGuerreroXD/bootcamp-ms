package co.com.bootcamp.drivenadapters.r2dbc.adapter;

import co.com.bootcamp.drivenadapters.r2dbc.mapper.BootcampCapacityEntityMapper;
import co.com.bootcamp.drivenadapters.r2dbc.repository.BootcampCapacityEntityRepository;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BootcampCapacityRepositoryAdapter implements BootcampCapacityRepository {
    private final BootcampCapacityEntityRepository entityRepository;
    private final BootcampCapacityEntityMapper mapper;

    @Override
    public Mono<BootcampCapacity> save(BootcampCapacity bootcampCapacity) {
        return entityRepository.save(mapper.toEntity(bootcampCapacity))
                .map(mapper::toModel);
    }

    @Override
    public Flux<BootcampCapacity> saveAll(Flux<BootcampCapacity> capacities) {
        return Flux.from(capacities)
                .map(mapper::toEntity)
                .transform(entityRepository::saveAll)
                .map(mapper::toModel);
    }

    @Override
    public Flux<BootcampCapacity> findByBootcampId(Long bootcampId) {
        return entityRepository.findByBootcampId(bootcampId).map(mapper::toModel);
    }

    @Override
    public Flux<BootcampCapacity> findByCapacityId(Long capacityId) {
        return entityRepository.findByCapacityId(capacityId).map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteByBootcampId(Long bootcampId) {
        return entityRepository.deleteByBootcampId(bootcampId);
    }

    @Override
    public Mono<Long> countByCapacityId(Long capacityId) {
        return entityRepository.countByCapacityId(capacityId);
    }

    @Override
    public Flux<Long> findCapacityIdsUsedOnlyByBootcamp(Long bootcampId) {
        return entityRepository.findCapacityIdsUsedOnlyByBootcamp(bootcampId);
    }
}