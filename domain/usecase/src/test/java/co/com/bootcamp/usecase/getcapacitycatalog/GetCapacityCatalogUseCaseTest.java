package co.com.bootcamp.usecase.getcapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCapacityCatalogUseCaseTest {

    @Mock
    private CapacityCatalogRepository capacityCatalogRepository;

    @Mock
    private GetTechnologyCapacityCatalogService getTechnologyCapacityCatalogService;

    private GetCapacityCatalogUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetCapacityCatalogUseCase(capacityCatalogRepository, getTechnologyCapacityCatalogService);
    }

    @Test
    void getAll_returnsAllCapacityCatalogs() {
        CapacityCatalog c1 = CapacityCatalog.builder().id(1L).externalId(1L).name("Java").build();
        CapacityCatalog c2 = CapacityCatalog.builder().id(2L).externalId(2L).name("Python").build();

        when(capacityCatalogRepository.findAll()).thenReturn(Flux.just(c1, c2));

        StepVerifier.create(useCase.getAll())
                .expectNext(c1)
                .expectNext(c2)
                .verifyComplete();
    }

    @Test
    void getAll_whenEmpty_returnsEmpty() {
        when(capacityCatalogRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAll())
                .verifyComplete();
    }
}