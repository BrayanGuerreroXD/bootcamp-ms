package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.SyncCapacityCatalogEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.mapper.SyncCapacityCatalogEventMapper;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.usecase.synccapacitycatalog.SyncCapacityCatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class SyncCapacityCatalogConsumer {
    private final SyncCapacityCatalogService syncCapacityCatalogService;
    private final SyncCapacityCatalogEventMapper mapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.sync-capacity-catalog}")
    public void consume(String message) {
        try {
            SyncCapacityCatalogEventDto dto = objectMapper.readValue(message, SyncCapacityCatalogEventDto.class);
            log.info("Processing capacity catalog event: {}", dto);

            CapacityCatalog capacityCatalog = mapper.toModel(dto);

            syncCapacityCatalogService.saveCapacityCatalogWithTechnologies(capacityCatalog)
                    .subscribe(
                            result -> log.info("Capacity catalog synced successfully: {}", result.getExternalId()),
                            error -> log.error("Error syncing capacity catalog: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error processing capacity catalog event: {}", e.getMessage(), e);
        }
    }
}