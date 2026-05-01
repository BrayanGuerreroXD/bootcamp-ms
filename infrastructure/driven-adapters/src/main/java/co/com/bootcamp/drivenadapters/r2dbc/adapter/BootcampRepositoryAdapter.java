package co.com.bootcamp.drivenadapters.r2dbc.adapter;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampEntity;
import co.com.bootcamp.drivenadapters.r2dbc.mapper.BootcampEntityMapper;
import co.com.bootcamp.drivenadapters.r2dbc.repository.BootcampEntityRepository;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.OrderDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BootcampRepositoryAdapter implements BootcampRepository {
    private final BootcampEntityRepository entityRepository;
    private final BootcampEntityMapper mapper;

    @Override
    public Mono<Bootcamp> save(Bootcamp bootcamp) {
        BootcampEntity entity = mapper.toEntity(bootcamp);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
            entity.setUpdatedAt(LocalDateTime.now());
        }
        return entityRepository.save(entity).map(mapper::toModel);
    }

    @Override
    public Mono<Bootcamp> findById(Long id) {
        return entityRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Flux<Bootcamp> findAll() {
        return entityRepository.findAll().map(mapper::toModel);
    }

    @Override
    public Flux<Bootcamp> findAllOrderByName(OrderDirection direction) {
        return direction == OrderDirection.ASC
            ? entityRepository.findAllByOrderByNameAsc().map(mapper::toModel)
            : entityRepository.findAllByOrderByNameDesc().map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return entityRepository.deleteById(id);
    }

    @Override
    public Mono<Long> countByCapacityId(Long capacityId) {
        return entityRepository.countByCapacityId(capacityId);
    }
}