package co.com.bootcamp.usecase.createbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Mono;

public interface CreateBootcampService {
    Mono<Bootcamp> create(Bootcamp bootcamp);
}