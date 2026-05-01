package co.com.bootcamp.usecase.getauth;

import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.auth.gateways.AuthRepository;
import co.com.bootcamp.model.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuthUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private GetAuthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAuthUseCase(authRepository);
    }

    @Test
    void getByToken_validToken_returnsLoggedUser() {
        String token = "validToken";
        Auth auth = Auth.builder()
                .id(1L)
                .email("test@example.com")
                .token(token)
                .expiresIn(null)
                .createdAt(LocalDateTime.now())
                .build();

        when(authRepository.findByToken(token)).thenReturn(Mono.just(auth));

        StepVerifier.create(useCase.getByToken(token))
                .expectNextMatches(user -> user.getEmail().equals("test@example.com"))
                .verifyComplete();
    }

    @Test
    void getByToken_invalidToken_returnsUnauthorized() {
        String token = "invalidToken";

        when(authRepository.findByToken(token)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getByToken(token))
                .expectError(UnauthorizedException.class)
                .verify();
    }

    @Test
    void getByToken_expiredToken_returnsUnauthorized() {
        String token = "expiredToken";
        Auth auth = Auth.builder()
                .id(1L)
                .email("test@example.com")
                .token(token)
                .expiresIn(1)
                .createdAt(LocalDateTime.now().minusSeconds(120))
                .build();

        when(authRepository.findByToken(token)).thenReturn(Mono.just(auth));

        StepVerifier.create(useCase.getByToken(token))
                .expectError(UnauthorizedException.class)
                .verify();
    }
}