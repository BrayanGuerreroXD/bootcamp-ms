package co.com.bootcamp.usecase.createbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.event.BootcampCapacityMatchEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class CreateBootcampUseCase implements CreateBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final EventGateway eventGateway;
    private final UserContext userContext;

    @Override
    public Mono<Bootcamp> create(Bootcamp bootcamp) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return bootcampRepository.save(bootcamp)
                            .flatMap(saved -> {
                                if (bootcamp.getCapacities() != null && !bootcamp.getCapacities().isEmpty()) {
                                    return Flux.fromIterable(bootcamp.getCapacities())
                                            .map(cap -> BootcampCapacity.builder()
                                                    .bootcamp(saved)
                                                    .capacity(cap)
                                                    .build())
                                            .transform(bootcampCapacityRepository::saveAll)
                                            .then(Mono.fromCallable(() -> {
                                                List<Long> capacityIds = bootcamp.getCapacities().stream()
                                                        .map(CapacityCatalog::getId)
                                                        .toList();
                                                BootcampCapacityMatchEvent event = BootcampCapacityMatchEvent.builder()
                                                        .bootcampId(saved.getId())
                                                        .capacityIds(capacityIds)
                                                        .build();
                                                eventGateway.publishBootcampCapacityMatch(event)
                                                        .subscribe();
                                                return saved;
                                            }));
                                }
                                return Mono.just(saved);
                            });
                });
    }
}