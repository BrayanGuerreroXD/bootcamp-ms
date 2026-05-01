package co.com.bootcamp.usecase.updatebootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Mono;

public interface UpdateBootcampService {
    Mono<Bootcamp> update(Long id, Bootcamp bootcamp);
}