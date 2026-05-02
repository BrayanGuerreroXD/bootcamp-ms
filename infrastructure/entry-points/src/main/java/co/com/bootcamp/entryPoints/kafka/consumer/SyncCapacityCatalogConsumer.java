package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.SyncCapacityCatalogEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.mapper.SyncCapacityCatalogEventMapper;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.usecase.synccapacitycatalog.SyncCapacityCatalogService;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncCapacityCatalogConsumer {
    private final SyncCapacityCatalogService syncCapacityCatalogService;
    private final SyncCapacityCatalogEventMapper mapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.sync-capacity-catalog}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            SyncCapacityCatalogEventDto dto = objectMapper.readValue(record.value(), SyncCapacityCatalogEventDto.class);
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