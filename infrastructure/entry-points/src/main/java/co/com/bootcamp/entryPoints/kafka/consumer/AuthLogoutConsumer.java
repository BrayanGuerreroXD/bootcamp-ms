package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.usecase.deleteauth.DeleteAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthLogoutConsumer {
    private final DeleteAuthService deleteAuthService;
    private final AuthLogoutEventMapper mapper;

    public AuthLogoutConsumer(DeleteAuthService deleteAuthService, AuthLogoutEventMapper mapper) {
        this.deleteAuthService = deleteAuthService;
        this.mapper = mapper;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "${kafka.topics.generic-auth-logout}")
    public void consume(String message) {
        try {
            AuthLogoutEvent event = mapper.toEvent(message);
            log.info("Processing auth logout event for email: {}", event.getEmail());

            deleteAuthService.deleteByEmailAndToken(event.getEmail(), event.getToken())
                    .subscribe(
                            () -> log.info("Auth deleted successfully for email: {}", event.getEmail()),
                            error -> log.error("Error deleting auth logout: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error processing auth logout message: {}", e.getMessage());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthLogoutEvent {
        private String email;
        private String token;
    }

    @Component
    public static class AuthLogoutEventMapper {
        public AuthLogoutEvent toEvent(String message) throws JsonProcessingException {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(message, AuthLogoutEvent.class);
        }
    }
}