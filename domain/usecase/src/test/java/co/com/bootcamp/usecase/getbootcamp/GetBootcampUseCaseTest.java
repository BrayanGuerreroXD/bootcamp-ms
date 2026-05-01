package co.com.bootcamp.usecase.getbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.OrderDirection;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
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
class GetBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;

    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;

    private GetBootcampUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetBootcampUseCase(bootcampRepository, bootcampCapacityRepository);
    }

    @Test
    void getById_WhenExists_ShouldReturnWithCapacities() {
        Bootcamp bootcamp = Bootcamp.builder().id(1L).name("Test").build();
        BootcampCapacity bc = BootcampCapacity.builder()
                .id(1L)
                .capacity(CapacityCatalog.builder().id(1L).name("Backend").build())
                .build();

        when(bootcampRepository.findById(1L)).thenReturn(Mono.just(bootcamp));
        when(bootcampCapacityRepository.findByBootcampId(1L)).thenReturn(Flux.just(bc));

        StepVerifier.create(useCase.getById(1L))
                .expectNextMatches(result ->
                    result.getId().equals(1L) &&
                    result.getCapacities() != null &&
                    result.getCapacities().size() == 1)
                .verifyComplete();
    }

    @Test
    void getById_WhenNotExists_ShouldReturnEmpty() {
        when(bootcampRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getById(99L))
                .verifyComplete();
    }

    @Test
    void getAll_ShouldReturnPaginatedResults() {
        Bootcamp bootcamp1 = Bootcamp.builder().id(1L).name("Alpha").build();
        Bootcamp bootcamp2 = Bootcamp.builder().id(2L).name("Beta").build();

        when(bootcampRepository.findAllOrderByName(OrderDirection.ASC)).thenReturn(Flux.just(bootcamp1, bootcamp2));
        when(bootcampCapacityRepository.findByBootcampId(any())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAll(0, 10, "name", "ASC").collectList())
                .expectNextMatches(results -> results.size() == 2)
                .verifyComplete();
    }
}