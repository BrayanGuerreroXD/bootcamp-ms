package co.com.bootcamp.drivenAdapters.r2dbc.repository;

import co.com.bootcamp.drivenAdapters.r2dbc.entity.AuthEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthEntityRepository extends ReactiveCrudRepository<AuthEntity, Long> {
    Mono<AuthEntity> findByToken(String token);
    Mono<AuthEntity> findByEmailAndToken(String email, String token);
    Mono<Void> deleteByEmailAndToken(String email, String token);
}