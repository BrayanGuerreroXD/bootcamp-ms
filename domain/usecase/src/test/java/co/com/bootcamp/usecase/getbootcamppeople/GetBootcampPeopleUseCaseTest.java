package co.com.bootcamp.usecase.getbootcamppeople;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBootcampPeopleUseCaseTest {

    @Mock
    private BootcampPeopleRepository bootcampPeopleRepository;

    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;

    @Mock
    private UserContext userContext;

    private GetBootcampPeopleUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetBootcampPeopleUseCase(bootcampPeopleRepository, bootcampCapacityRepository, userContext);
    }

    @Test
    void getByEmail_WhenNonAdminUser_ShouldReturnBootcamps() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();
        Bootcamp bootcamp = Bootcamp.builder().id(1L).name("Java Bootcamp").build();
        BootcampPeople people = BootcampPeople.builder().id(1L).email("user@test.com").bootcamp(bootcamp).build();
        BootcampCapacity bc = BootcampCapacity.builder()
                .id(1L)
                .capacity(CapacityCatalog.builder().id(1L).name("Backend").build())
                .build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.findByEmail("user@test.com")).thenReturn(Flux.just(people));
        when(bootcampCapacityRepository.findByBootcampId(1L)).thenReturn(Flux.just(bc));

        StepVerifier.create(useCase.getByEmail("user@test.com", 0, 10).collectList())
                .expectNextMatches(results ->
                    results.size() == 1 &&
                    results.get(0).getBootcamp().getCapacities() != null)
                .verifyComplete();
    }

    @Test
    void getByEmail_WhenAdminUser_ShouldReturnForbidden() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").name("Admin").isAdmin(true).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));

        StepVerifier.create(useCase.getByEmail("admin@test.com", 0, 10))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void getByEmail_WhenNoResults_ShouldReturnEmpty() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.findByEmail("user@test.com")).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getByEmail("user@test.com", 0, 10).collectList())
                .expectNextMatches(results -> results.isEmpty())
                .verifyComplete();
    }
}
