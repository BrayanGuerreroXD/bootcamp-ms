package co.com.bootcamp.usecase.getfullbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import reactor.core.publisher.Mono;

public interface GetFullBootcampService {
    Mono<Bootcamp> getFullBootcamp(Long id);
    Mono<Void> publishReport(Long bootcampId);
}
