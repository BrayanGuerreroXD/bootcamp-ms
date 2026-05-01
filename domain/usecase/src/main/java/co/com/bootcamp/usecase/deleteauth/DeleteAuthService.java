package co.com.bootcamp.usecase.deleteauth;

import reactor.core.publisher.Mono;

public interface DeleteAuthService {
    Mono<Void> deleteByEmailAndToken(String email, String token);
}