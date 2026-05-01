package co.com.bootcamp.usecase.saveauth;

import co.com.bootcamp.model.auth.Auth;
import reactor.core.publisher.Mono;

public interface SaveAuthService {
    Mono<Auth> save(Auth auth);
}