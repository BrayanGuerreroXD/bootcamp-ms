package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.usecase.synccapacitycatalog.SyncCapacityCatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SyncCapacityCatalogServiceEntryPointTest {

    @Mock
    private SyncCapacityCatalogService syncCapacityCatalogService;

    private SyncCapacityCatalogServiceEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        entryPoint = new SyncCapacityCatalogServiceEntryPoint(syncCapacityCatalogService);
    }

    @Test
    void processCapacityCatalogEvent_savesCapacityWithTechnologies() {
        String jsonMessage = """
            {
                "id": 1,
                "name": "Java",
                "technologies": [
                    {"id": 10, "name": "Spring"},
                    {"id": 11, "name": "Hibernate"}
                ]
            }
            """;

        CapacityCatalog savedCatalog = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java")
                .technologies(List.of())
                .build();

        when(syncCapacityCatalogService.saveCapacityCatalogWithTechnologies(any()))
                .thenReturn(Mono.just(savedCatalog));

        entryPoint.processCapacityCatalogEvent(jsonMessage);

        verify(syncCapacityCatalogService).saveCapacityCatalogWithTechnologies(any());
    }

    @Test
    void processCapacityCatalogEvent_withEmptyTechnologies_savesCapacity() {
        String jsonMessage = """
            {
                "id": 1,
                "name": "Java",
                "technologies": []
            }
            """;

        CapacityCatalog savedCatalog = CapacityCatalog.builder()
                .id(1L)
                .externalId(1L)
                .name("Java")
                .build();

        when(syncCapacityCatalogService.saveCapacityCatalogWithTechnologies(any()))
                .thenReturn(Mono.just(savedCatalog));

        entryPoint.processCapacityCatalogEvent(jsonMessage);

        verify(syncCapacityCatalogService).saveCapacityCatalogWithTechnologies(any());
    }
}