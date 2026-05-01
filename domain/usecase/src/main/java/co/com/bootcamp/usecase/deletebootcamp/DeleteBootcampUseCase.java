package co.com.bootcamp.usecase.deletebootcamp;

import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.event.BootcampDeleteMatchEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.security.UserContext;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class DeleteBootcampUseCase implements DeleteBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final EventGateway eventGateway;
    private final UserContext userContext;

    @Override
    public Mono<Void> delete(Long id) {
        return userContext.currentUser()
                .flatMap(user -> {
                    if (Boolean.FALSE.equals(user.getIsAdmin())) {
                        return Mono.error(new ForbiddenException(GlobalExceptionEnum.FORBIDDEN_ACCESS));
                    }
                    return bootcampCapacityRepository.findCapacityIdsUsedOnlyByBootcamp(id)
                            .collectList()
                            .flatMap(uniqueCapacityIds -> {
                                if (uniqueCapacityIds != null && !uniqueCapacityIds.isEmpty()) {
                                    BootcampDeleteMatchEvent event = BootcampDeleteMatchEvent.builder()
                                            .bootcampId(id)
                                            .capacityIds(uniqueCapacityIds)
                                            .build();
                                    return eventGateway.publishBootcampDeleteMatch(event)
                                            .then(bootcampRepository.deleteById(id));
                                }
                                return bootcampRepository.deleteById(id);
                            });
                });
    }
}