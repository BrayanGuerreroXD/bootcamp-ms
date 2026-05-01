package co.com.bootcamp.usecase.deleteauth;

import co.com.bootcamp.model.auth.gateways.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteAuthUseCaseTest {

    @Mock
    private AuthRepository authRepository;

    private DeleteAuthUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteAuthUseCase(authRepository);
    }

    @Test
    void deleteByEmailAndToken_deletesSuccessfully() {
        String email = "test@example.com";
        String token = "token123";

        when(authRepository.deleteByEmailAndToken(email, token)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.deleteByEmailAndToken(email, token))
                .verifyComplete();

        verify(authRepository).deleteByEmailAndToken(email, token);
    }
}