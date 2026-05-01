package co.com.bootcamp.usecase.synctechnologycapacitycatalog;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncTechnologyCapacityCatalogUseCaseTest {

    @Mock
    private TechnologyCapacityCatalogRepository technologyRepository;

    private SyncTechnologyCapacityCatalogUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SyncTechnologyCapacityCatalogUseCase(technologyRepository);
    }

    @Test
    void saveAll_deletesAndRecreates() {
        Long capacityCatalogId = 1L;
        List<TechnologyCapacityCatalog> technologies = List.of(
                TechnologyCapacityCatalog.builder().externalId(10L).name("Spring").build(),
                TechnologyCapacityCatalog.builder().externalId(11L).name("Hibernate").build()
        );

        TechnologyCapacityCatalog saved1 = TechnologyCapacityCatalog.builder()
                .id(1L).capacityCatalogId(1L).externalId(10L).name("Spring").build();
        TechnologyCapacityCatalog saved2 = TechnologyCapacityCatalog.builder()
                .id(2L).capacityCatalogId(1L).externalId(11L).name("Hibernate").build();

        when(technologyRepository.deleteByCapacityCatalogId(capacityCatalogId)).thenReturn(Mono.empty());
        when(technologyRepository.saveAll(any())).thenReturn(Flux.just(saved1, saved2));

        StepVerifier.create(useCase.saveAll(capacityCatalogId, technologies))
                .expectNextCount(2)
                .verifyComplete();

        verify(technologyRepository).deleteByCapacityCatalogId(capacityCatalogId);
    }
}