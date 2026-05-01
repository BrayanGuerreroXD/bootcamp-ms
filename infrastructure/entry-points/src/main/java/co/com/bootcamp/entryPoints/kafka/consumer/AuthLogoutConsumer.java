package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLogoutEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.mapper.AuthLogoutEventMapper;
import co.com.bootcamp.usecase.deleteauth.DeleteAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthLogoutConsumer {
    private final DeleteAuthService deleteAuthService;
    private final AuthLogoutEventMapper mapper;

    @KafkaListener(topics = "${kafka.topics.generic-auth-logout}")
    public void consume(String message) {
        try {
            AuthLogoutEventDto dto = mapper.toDto(message);
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