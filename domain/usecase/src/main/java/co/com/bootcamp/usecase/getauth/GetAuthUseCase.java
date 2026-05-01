package co.com.bootcamp.usecase.getauth;

import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.auth.gateways.AuthRepository;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;

@RequiredArgsConstructor
public class GetAuthUseCase implements GetAuthService {
    private final AuthRepository authRepository;

    @Override
    public Mono<LoggedUser> getByToken(String token) {
        return authRepository.findByToken(token)
                .filter(this::isTokenValid)
                .map(auth -> LoggedUser.builder().email(auth.getEmail()).name(auth.getName()).build())
                .switchIfEmpty(Mono.error(new UnauthorizedException(GlobalExceptionEnum.UNAUTHORIZED)));
    }

    private boolean isTokenValid(Auth auth) {
        if (auth.getExpiresIn() == null) return true;
        long now = Instant.now().getEpochSecond();
        long created = auth.getCreatedAt().toEpochSecond(ZoneOffset.UTC);
        return now - created < auth.getExpiresIn();
    }
}