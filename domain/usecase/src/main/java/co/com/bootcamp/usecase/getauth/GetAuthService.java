package co.com.bootcamp.usecase.getauth;

import co.com.bootcamp.model.auth.LoggedUser;
import reactor.core.publisher.Mono;

public interface GetAuthService {
    Mono<LoggedUser> getByToken(String token);
}