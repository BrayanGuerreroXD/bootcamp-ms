package co.com.bootcamp.usecase.signupbootcamp;

import co.com.bootcamp.model.bootcamp.BootcampPeople;
import reactor.core.publisher.Mono;

public interface SignUpBootcampService {
    Mono<BootcampPeople> signUp(Long bootcampId);
}