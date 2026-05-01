package co.com.bootcamp.usecase.getbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GetBootcampService {
    Mono<Bootcamp> getById(Long id);
    Flux<Bootcamp> getAll(int page, int size, String sortBy, String direction);
}