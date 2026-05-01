package co.com.bootcamp.usecase.synccapacitycatalog;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncCapacityCatalogUseCaseTest {

    @Mock
    private CapacityCatalogRepository capacityCatalogRepository;

    @Mock
    private TechnologyCapacityCatalogRepository technologyRepository;

    private SyncCapacityCatalogUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SyncCapacityCatalogUseCase(capacityCatalogRepository, technologyRepository);
    }

    @Test
    void syncCapacityCatalog_whenNotExists_createsNew() {
        CapacityCatalog input = CapacityCatalog.builder()
                .externalId(1L)
                .name("Java")
                .build();

        when(capacityCatalogRepository.findByExternalId(1L)).thenReturn(Mono.empty());
        when(capacityCatalogRepository.save(any())).thenReturn(Mono.just(input));

        StepVerifier.create(useCase.syncCapacityCatalog(input))
                .expectNext(input)
                .verifyComplete();
    }

    @Test
    void syncCapacityCatalog_whenExists_updates() {
        CapacityCatalog existing = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java")
                .build();

        CapacityCatalog updated = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java Updated")
                .build();

        CapacityCatalog input = CapacityCatalog.builder()
                .externalId(1L)
                .name("Java Updated")
                .build();

        when(capacityCatalogRepository.findByExternalId(1L)).thenReturn(Mono.just(existing));
        when(capacityCatalogRepository.save(any())).thenReturn(Mono.just(updated));

        StepVerifier.create(useCase.syncCapacityCatalog(input))
                .expectNextMatches(result -> result.getName().equals("Java Updated"))
                .verifyComplete();
    }

    @Test
    void saveCapacityCatalogWithTechnologies_savesTechnologies() {
        CapacityCatalog input = CapacityCatalog.builder()
                .externalId(1L)
                .name("Java")
                .technologies(List.of(
                        TechnologyCapacityCatalog.builder().externalId(10L).name("Spring").build(),
                        TechnologyCapacityCatalog.builder().externalId(11L).name("Hibernate").build()
                ))
                .build();

        CapacityCatalog savedCatalog = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java")
                .build();

        when(capacityCatalogRepository.findByExternalId(1L)).thenReturn(Mono.empty());
        when(capacityCatalogRepository.save(any())).thenReturn(Mono.just(savedCatalog));
        when(technologyRepository.deleteByCapacityCatalogId(1L)).thenReturn(Mono.empty());
        when(technologyRepository.saveAll(any())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.saveCapacityCatalogWithTechnologies(input))
                .expectNext(savedCatalog)
                .verifyComplete();

        verify(technologyRepository).deleteByCapacityCatalogId(1L);
    }

    @Test
    void getAllCapacityCatalogs_returnsAll() {
        CapacityCatalog c1 = CapacityCatalog.builder().id(1L).externalId(1L).name("Java").build();
        CapacityCatalog c2 = CapacityCatalog.builder().id(2L).externalId(2L).name("Python").build();

        when(capacityCatalogRepository.findAll()).thenReturn(Flux.just(c1, c2));

        StepVerifier.create(useCase.getAllCapacityCatalogs())
                .expectNextMatches(list -> list.size() == 2)
                .verifyComplete();
    }
}