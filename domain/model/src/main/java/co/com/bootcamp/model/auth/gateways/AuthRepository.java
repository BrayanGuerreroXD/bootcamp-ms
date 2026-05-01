package co.com.bootcamp.model.auth.gateways;

import co.com.bootcamp.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface AuthRepository {
    Mono<Auth> save(Auth auth);
    Mono<Auth> findByToken(String token);
    Mono<Auth> findByEmailAndToken(String email, String token);
    Mono<Void> deleteByEmailAndToken(String email, String token);
}