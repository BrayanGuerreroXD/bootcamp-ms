package co.com.bootcamp.usecase.getbootcamppeople;

import co.com.bootcamp.model.bootcamp.BootcampPeople;
import reactor.core.publisher.Flux;

public interface GetBootcampPeopleService {
    Flux<BootcampPeople> getByEmail(String email, int page, int size);
}
