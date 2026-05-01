package co.com.bootcamp.usecase.deletebootcamp;

import reactor.core.publisher.Mono;

public interface DeleteBootcampService {
    Mono<Void> delete(Long id);
}