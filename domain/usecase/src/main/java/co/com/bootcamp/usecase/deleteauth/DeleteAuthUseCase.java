package co.com.bootcamp.usecase.deleteauth;

import co.com.bootcamp.model.auth.gateways.AuthRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteAuthUseCase implements DeleteAuthService {
    private final AuthRepository authRepository;

    @Override
    public Mono<Void> deleteByEmailAndToken(String email, String token) {
        return authRepository.deleteByEmailAndToken(email, token);
    }
}