package co.com.bootcamp.usecase.gettechnologycapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTechnologyCapacityCatalogUseCaseTest {

    @Mock
    private CapacityCatalogRepository capacityCatalogRepository;

    @Mock
    private TechnologyCapacityCatalogRepository technologyRepository;

    private GetTechnologyCapacityCatalogUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTechnologyCapacityCatalogUseCase(capacityCatalogRepository, technologyRepository);
    }

    @Test
    void getAllByCapacityId_returnsCapacityWithTechnologies() {
        CapacityCatalog capacity = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java")
                .build();

        List<TechnologyCapacityCatalog> technologies = List.of(
                TechnologyCapacityCatalog.builder().id(1L).capacityCatalogId(1L).externalId(10L).name("Spring").build(),
                TechnologyCapacityCatalog.builder().id(2L).capacityCatalogId(1L).externalId(11L).name("Hibernate").build()
        );

        when(capacityCatalogRepository.findById(1L)).thenReturn(Mono.just(capacity));
        when(technologyRepository.findByCapacityCatalogId(1L)).thenReturn(Flux.just(technologies.get(0), technologies.get(1)));

        StepVerifier.create(useCase.getAllByCapacityId(1L))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getTechnologies().size() == 2)
                .verifyComplete();
    }

    @Test
    void getAllByCapacityId_whenNotFound_returnsEmpty() {
        when(capacityCatalogRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getAllByCapacityId(99L))
                .verifyComplete();
    }

    @Test
    void getAllTechnologies_returnsAll() {
        TechnologyCapacityCatalog t1 = TechnologyCapacityCatalog.builder().id(1L).externalId(10L).name("Spring").build();
        TechnologyCapacityCatalog t2 = TechnologyCapacityCatalog.builder().id(2L).externalId(11L).name("Hibernate").build();

        when(technologyRepository.findAll()).thenReturn(Flux.just(t1, t2));

        StepVerifier.create(useCase.getAllTechnologies())
                .expectNext(t1)
                .expectNext(t2)
                .verifyComplete();
    }
}