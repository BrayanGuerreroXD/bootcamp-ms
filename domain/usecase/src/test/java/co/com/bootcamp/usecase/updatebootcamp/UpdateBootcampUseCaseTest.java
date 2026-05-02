package co.com.bootcamp.usecase.updatebootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.NotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;

    @Mock
    private UserContext userContext;

    @Mock
    private EventGateway eventGateway;

    private UpdateBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBootcampUseCase(bootcampRepository, bootcampCapacityRepository, eventGateway, userContext);
    }

    @Test
    void updateBootcamp_WhenAdminUser_ShouldUpdateSuccessfully() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").name("Admin").isAdmin(true).build();
        Bootcamp existing = Bootcamp.builder().id(1L).name("Old Name").build();
        Bootcamp updateData = Bootcamp.builder()
                .name("New Name")
                .description("New Desc")
                .initTime(LocalDateTime.now())
                .duration(30)
                .capacities(List.of(CapacityCatalog.builder().id(1L).name("Backend").build()))
                .build();
        Bootcamp updated = updateData.toBuilder().id(1L).capacityCount(1).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampRepository.findById(1L)).thenReturn(Mono.just(existing));
        when(bootcampRepository.save(any(Bootcamp.class))).thenReturn(Mono.just(updated));
        when(bootcampCapacityRepository.deleteByBootcampId(1L)).thenReturn(Mono.empty());
        when(bootcampCapacityRepository.saveAll(any())).thenReturn(Flux.empty());
        when(eventGateway.publishCapacitiesBootcampsMatch(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.update(1L, updateData))
                .expectNextMatches(result -> result.getName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    void updateBootcamp_WhenNonAdminUser_ShouldReturnForbidden() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").name("User").isAdmin(false).build();
        Bootcamp bootcamp = Bootcamp.builder().name("Test").build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));

        StepVerifier.create(useCase.update(1L, bootcamp))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void updateBootcamp_WhenNotFound_ShouldReturnNotFound() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").name("Admin").isAdmin(true).build();
        Bootcamp bootcamp = Bootcamp.builder().name("Test").build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.update(99L, bootcamp))
                .expectError(NotFoundException.class)
                .verify();
    }
}