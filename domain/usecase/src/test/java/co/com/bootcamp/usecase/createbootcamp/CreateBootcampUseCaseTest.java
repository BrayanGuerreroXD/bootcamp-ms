package co.com.bootcamp.usecase.createbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;

    @Mock
    private UserContext userContext;

    private CreateBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateBootcampUseCase(bootcampRepository, bootcampCapacityRepository, userContext);
    }

    @Test
    void createBootcamp_WhenAdminUser_ShouldCreateSuccessfully() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").isAdmin(true).build();
        Bootcamp bootcamp = Bootcamp.builder()
                .name("Java Bootcamp")
                .description("Learn Java")
                .initTime(LocalDateTime.now())
                .duration(30)
                .capacities(List.of(CapacityCatalog.builder().id(1L).name("Backend").build()))
                .build();
        Bootcamp savedBootcamp = bootcamp.toBuilder().id(1L).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampRepository.save(any(Bootcamp.class))).thenReturn(Mono.just(savedBootcamp));
        when(bootcampCapacityRepository.saveAll(any())).thenReturn(reactor.core.publisher.Flux.empty());

        StepVerifier.create(useCase.create(bootcamp))
                .expectNextMatches(result -> result.getId().equals(1L))
                .verifyComplete();
    }

    @Test
    void createBootcamp_WhenNonAdminUser_ShouldReturnForbidden() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();
        Bootcamp bootcamp = Bootcamp.builder().name("Test").build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));

        StepVerifier.create(useCase.create(bootcamp))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void createBootcamp_WhenNoCapacities_ShouldCreateWithoutCapacities() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").isAdmin(true).build();
        Bootcamp bootcamp = Bootcamp.builder().name("Test").build();
        Bootcamp savedBootcamp = bootcamp.toBuilder().id(1L).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));
        when(bootcampRepository.save(any(Bootcamp.class))).thenReturn(Mono.just(savedBootcamp));

        StepVerifier.create(useCase.create(bootcamp))
                .expectNextMatches(result -> result.getId().equals(1L))
                .verifyComplete();
    }
}