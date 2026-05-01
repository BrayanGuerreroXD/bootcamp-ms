package co.com.bootcamp.entryPoints.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class SyncCapacityCatalogConsumer {
    private final SyncCapacityCatalogServiceEntryPoint syncCapacityCatalogServiceEntryPoint;

    @KafkaListener(topics = "${kafka.topics.sync.capacity.catalog}")
    public void consume(String message) {
        log.info("Received capacity catalog sync event: {}", message);
        syncCapacityCatalogServiceEntryPoint.processCapacityCatalogEvent(message);
    }
}