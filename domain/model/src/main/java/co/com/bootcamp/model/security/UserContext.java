package co.com.bootcamp.model.security;

import co.com.bootcamp.model.auth.LoggedUser;
import reactor.core.publisher.Mono;

public interface UserContext {
    Mono<LoggedUser> currentUser();
}