package co.com.bootcamp.entryPoints.api.security;

import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveUserContext implements UserContext {

    @Override
    public Mono<LoggedUser> currentUser() {
        return Mono.fromCallable(() -> {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof LoggedUser) {
                return (LoggedUser) authentication.getPrincipal();
            }
            return null;
        });
    }
}