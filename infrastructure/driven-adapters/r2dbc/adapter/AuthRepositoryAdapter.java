package co.com.bootcamp.drivenAdapters.r2dbc.adapter;

import co.com.bootcamp.drivenAdapters.r2dbc.mapper.AuthEntityMapper;
import co.com.bootcamp.drivenAdapters.r2dbc.repository.AuthEntityRepository;
import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.model.auth.gateways.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryAdapter implements AuthRepository {
    private final AuthEntityRepository entityRepository;
    private final AuthEntityMapper mapper;

    @Override
    public Mono<Auth> save(Auth auth) {
        AuthEntity entity = mapper.toEntity(auth);
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        return entityRepository.save(entity)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Auth> findByToken(String token) {
        return entityRepository.findByToken(token)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Auth> findByEmailAndToken(String email, String token) {
        return entityRepository.findByEmailAndToken(email, token)
                .map(mapper::toModel);
    }

    @Override
    public Mono<Void> deleteByEmailAndToken(String email, String token) {
        return entityRepository.findByEmailAndToken(email, token)
                .flatMap(entity -> entityRepository.deleteById(entity.getId()))
                .then();
    }
}