package co.com.bootcamp.usecase.signupbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.exception.BadRequestException;
import co.com.bootcamp.model.exception.ConflictException;
import co.com.bootcamp.model.exception.ForbiddenException;
import co.com.bootcamp.model.exception.NotFoundException;
import co.com.bootcamp.model.auth.LoggedUser;
import co.com.bootcamp.model.security.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignUpBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampPeopleRepository bootcampPeopleRepository;

    @Mock
    private UserContext userContext;

    private SignUpBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SignUpBootcampUseCase(bootcampRepository, bootcampPeopleRepository, userContext);
    }

    @Test
    void signUp_WhenNonAdminUser_ShouldEnrollSuccessfully() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();
        Bootcamp bootcamp = Bootcamp.builder().id(1L).name("Java Bootcamp").build();
        BootcampPeople saved = BootcampPeople.builder().id(1L).email("user@test.com").bootcamp(bootcamp).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.countByEmail("user@test.com")).thenReturn(Mono.just(0L));
        when(bootcampPeopleRepository.existsByBootcampIdAndEmail(1L, "user@test.com")).thenReturn(Mono.just(false));
        when(bootcampRepository.findById(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampPeopleRepository.save(any(BootcampPeople.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.signUp(1L, "user@test.com"))
                .expectNextMatches(result -> result.getEmail().equals("user@test.com"))
                .verifyComplete();
    }

    @Test
    void signUp_WhenAdminUser_ShouldReturnForbidden() {
        LoggedUser adminUser = LoggedUser.builder().email("admin@test.com").isAdmin(true).build();

        when(userContext.currentUser()).thenReturn(Mono.just(adminUser));

        StepVerifier.create(useCase.signUp(1L, "admin@test.com"))
                .expectError(ForbiddenException.class)
                .verify();
    }

    @Test
    void signUp_WhenAlreadyEnrolled_ShouldReturnConflict() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.countByEmail("user@test.com")).thenReturn(Mono.just(0L));
        when(bootcampPeopleRepository.existsByBootcampIdAndEmail(1L, "user@test.com")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.signUp(1L, "user@test.com"))
                .expectError(ConflictException.class)
                .verify();
    }

    @Test
    void signUp_WhenMaxEnrollmentsReached_ShouldReturnBadRequest() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.countByEmail("user@test.com")).thenReturn(Mono.just(5L));

        StepVerifier.create(useCase.signUp(1L, "user@test.com"))
                .expectError(BadRequestException.class)
                .verify();
    }

    @Test
    void signUp_WhenBootcampNotFound_ShouldReturnNotFound() {
        LoggedUser regularUser = LoggedUser.builder().email("user@test.com").isAdmin(false).build();

        when(userContext.currentUser()).thenReturn(Mono.just(regularUser));
        when(bootcampPeopleRepository.countByEmail("user@test.com")).thenReturn(Mono.just(0L));
        when(bootcampPeopleRepository.existsByBootcampIdAndEmail(99L, "user@test.com")).thenReturn(Mono.just(false));
        when(bootcampRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.signUp(99L, "user@test.com"))
                .expectError(NotFoundException.class)
                .verify();
    }
}