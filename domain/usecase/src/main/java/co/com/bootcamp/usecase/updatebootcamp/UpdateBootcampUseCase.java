package co.com.bootcamp.usecase.updatebootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.event.BootcampCapacityMatchEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.exception.NotFoundException;
import co.com.bootcamp.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class UpdateBootcampUseCase implements UpdateBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final EventGateway eventGateway;
    private final UserContext userContext;

    @Override
    public Mono<Bootcamp> update(Long id, Bootcamp bootcamp) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return bootcampRepository.findById(id)
                            .switchIfEmpty(Mono.error(new NotFoundException(GlobalExceptionEnum.NOT_FOUND)))
                            .flatMap(existing -> {
                                Bootcamp toUpdate = existing.toBuilder()
                                        .name(bootcamp.getName())
                                        .description(bootcamp.getDescription())
                                        .initTime(bootcamp.getInitTime())
                                        .duration(bootcamp.getDuration())
                                        .capacityCount(bootcamp.getCapacities() != null ? bootcamp.getCapacities().size() : 0)
                                        .build();
                                return bootcampRepository.save(toUpdate)
                                        .flatMap(saved -> bootcampCapacityRepository.deleteByBootcampId(id)
                                                .thenMany(Flux.fromIterable(bootcamp.getCapacities()))
                                                .map(cap -> BootcampCapacity.builder()
                                                        .bootcamp(saved)
                                                        .capacity(cap)
                                                        .build())
                                                .transform(bootcampCapacityRepository::saveAll)
                                                .then(Mono.fromCallable(() -> {
                                                    if (bootcamp.getCapacities() != null && !bootcamp.getCapacities().isEmpty()) {
                                                        List<Long> capacityIds = bootcamp.getCapacities().stream()
                                                                .map(CapacityCatalog::getId)
                                                                .toList();
                                                        BootcampCapacityMatchEvent event = BootcampCapacityMatchEvent.builder()
                                                                .bootcampId(saved.getId())
                                                                .capacityIds(capacityIds)
                                                                .build();
                                                        eventGateway.publishBootcampCapacityMatch(event)
                                                                .subscribe();
                                                    }
                                                    return saved;
                                                })));
                            });
                });
    }
}