package co.com.bootcamp.usecase.deletebootcamp;

import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.event.BootcampDeleteMatchEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;

    @Mock
    private EventGateway eventGateway;

    @Mock
    private UserContext userContext;

    private DeleteBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteBootcampUseCase(bootcampRepository, bootcampCapacityRepository, eventGateway, userContext);
    }

    @Test
    void deleteBootcamp_WhenAdminUser_ShouldDeleteSuccessfully() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").isAdmin(true).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampCapacityRepository.findCapacityIdsUsedOnlyByBootcamp(1L)).thenReturn(Flux.empty());
        when(bootcampRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.delete(1L))
                .verifyComplete();
    }

    @Test
    void deleteBootcamp_WhenNonAdminUser_ShouldReturnForbidden() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));

        StepVerifier.create(useCase.delete(1L))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void deleteBootcamp_WhenHasUniqueCapacities_ShouldPublishEvent() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").isAdmin(true).build();
        List<Long> uniqueCapacityIds = List.of(1L, 2L);

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampCapacityRepository.findCapacityIdsUsedOnlyByBootcamp(1L)).thenReturn(Flux.fromIterable(uniqueCapacityIds));
        when(eventGateway.publishBootcampDeleteMatch(any(BootcampDeleteMatchEvent.class))).thenReturn(Mono.empty());
        when(bootcampRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.delete(1L))
                .verifyComplete();
    }
}