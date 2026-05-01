package co.com.bootcamp.infrastructure.entryPoints.kafka.consumer;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.usecase.synccapacitycatalog.SyncCapacityCatalogService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SyncCapacityCatalogServiceEntryPoint {
    private final SyncCapacityCatalogService syncCapacityCatalogService;

    public SyncCapacityCatalogServiceEntryPoint(SyncCapacityCatalogService syncCapacityCatalogService) {
        this.syncCapacityCatalogService = syncCapacityCatalogService;
    }

    public void processCapacityCatalogEvent(String message) {
        try {
            SyncCapacityCatalogEvent event = parseEvent(message);
            log.info("Processing capacity catalog event: {}", event);

            List<TechnologyCapacityCatalog> technologies = event.getTechnologies().stream()
                    .map(tech -> TechnologyCapacityCatalog.builder()
                            .externalId(tech.getId())
                            .name(tech.getName())
                            .build())
                    .toList();

            CapacityCatalog capacityCatalog = CapacityCatalog.builder()
                    .externalId(event.getId())
                    .name(event.getName())
                    .technologies(technologies)
                    .build();

            syncCapacityCatalogService.saveCapacityCatalogWithTechnologies(capacityCatalog)
                    .subscribe(
                            result -> log.info("Capacity catalog synced successfully: {}", result.getExternalId()),
                            error -> log.error("Error syncing capacity catalog: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error processing capacity catalog event: {}", e.getMessage(), e);
        }
    }

    private SyncCapacityCatalogEvent parseEvent(String message) {
        return new com.fasterxml.jackson.databind.ObjectMapper().readValue(message, SyncCapacityCatalogEvent.class);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SyncCapacityCatalogEvent {
        private Long id;
        private String name;
        private List<TechnologyEvent> technologies;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TechnologyEvent {
        private Long id;
        private String name;
    }
}