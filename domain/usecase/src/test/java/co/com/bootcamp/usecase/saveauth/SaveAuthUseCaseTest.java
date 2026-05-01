package co.com.bootcamp.usecase.saveauth;

import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.model.auth.gateways.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveAuthUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private SaveAuthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SaveAuthUseCase(authRepository);
    }

    @Test
    void save_auth_returnsSavedAuth() {
        Auth auth = Auth.builder()
                .email("test@example.com")
                .token("token123")
                .isAdmin(false)
                .expiresIn(3600)
                .build();

        Auth savedAuth = Auth.builder()
                .id(1L)
                .email("test@example.com")
                .token("token123")
                .isAdmin(false)
                .expiresIn(3600)
                .createdAt(LocalDateTime.now())
                .build();

        when(authRepository.save(any())).thenReturn(Mono.just(savedAuth));

        StepVerifier.create(useCase.save(auth))
                .expectNext(savedAuth)
                .verifyComplete();
    }
}