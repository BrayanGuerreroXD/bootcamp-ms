package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLogoutEventDto;
import co.com.bootcamp.usecase.deleteauth.DeleteAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthLogoutConsumer {
    private final DeleteAuthService deleteAuthService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.generic-auth-logout}")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            AuthLogoutEventDto dto = objectMapper.readValue(record.value(), AuthLogoutEventDto.class);
            log.info("Processing auth logout event for email: {}", dto.getEmail());

            deleteAuthService.deleteByEmailAndToken(dto.getEmail(), dto.getToken())
                    .subscribe(
                            result -> log.info("Auth deleted successfully for email: {}", dto.getEmail()),
                            error -> log.error("Error deleting auth logout: {}", error.getMessage()),
                            () -> {}
                    );
        } catch (Exception e) {
            log.error("Error processing auth logout message: {}", e.getMessage());
        }
    }
}