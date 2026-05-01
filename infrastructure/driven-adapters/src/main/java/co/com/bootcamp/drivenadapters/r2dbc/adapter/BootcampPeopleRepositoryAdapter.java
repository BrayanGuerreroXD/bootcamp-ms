package co.com.bootcamp.drivenadapters.r2dbc.adapter;

import co.com.bootcamp.drivenadapters.r2dbc.mapper.BootcampPeopleEntityMapper;
import co.com.bootcamp.drivenadapters.r2dbc.repository.BootcampPeopleEntityRepository;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class BootcampPeopleRepositoryAdapter implements BootcampPeopleRepository {
    private final BootcampPeopleEntityRepository entityRepository;
    private final BootcampPeopleEntityMapper mapper;

    @Override
    public Mono<BootcampPeople> save(BootcampPeople bootcampPeople) {
        BootcampPeopleEntity entity = mapper.toEntity(bootcampPeople);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        return entityRepository.save(entity).map(mapper::toModel);
    }

    @Override
    public Flux<BootcampPeople> findByEmail(String email) {
        return entityRepository.findByEmail(email.toLowerCase()).map(mapper::toModel);
    }

    @Override
    public Mono<Long> countByEmail(String email) {
        return entityRepository.countByEmail(email.toLowerCase());
    }

    @Override
    public Mono<Boolean> existsByBootcampIdAndEmail(Long bootcampId, String email) {
        return entityRepository.existsByBootcampIdAndEmail(bootcampId, email.toLowerCase());
    }

    @Override
    public Mono<Void> deleteByBootcampId(Long bootcampId) {
        return entityRepository.deleteByBootcampId(bootcampId);
    }
}