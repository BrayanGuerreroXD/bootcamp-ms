package co.com.bootcamp.usecase.getbootcamppeople;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetBootcampPeopleUseCase implements GetBootcampPeopleService {
    private final BootcampPeopleRepository bootcampPeopleRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final UserContext userContext;

    @Override
    public Flux<BootcampPeople> getByEmail(String email, int page, int size) {
        return userContext.currentUser()
                .flatMapMany(user -> {
                    if (Boolean.TRUE.equals(user.getIsAdmin())) {
                        return Flux.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    String normalizedEmail = email.toLowerCase();
                    return bootcampPeopleRepository.findByEmail(normalizedEmail)
                            .skip(page * size)
                            .take(size)
                            .flatMap(this::enrichWithCapacities);
                });
    }

    private reactor.core.publisher.Mono<BootcampPeople> enrichWithCapacities(BootcampPeople people) {
        return bootcampCapacityRepository.findByBootcampId(people.getBootcamp().getId())
                .map(bc -> bc.getCapacity())
                .collectList()
                .map(capacities -> people.toBuilder()
                        .bootcamp(people.getBootcamp().toBuilder()
                                .capacities(capacities)
                                .capacityCount(capacities.size())
                                .build())
                        .build());
    }
}
