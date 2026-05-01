package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.usecase.saveauth.SaveAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AuthLoginConsumer {
    private final SaveAuthService saveAuthService;
    private final AuthLoginEventMapper mapper;

    public AuthLoginConsumer(SaveAuthService saveAuthService, AuthLoginEventMapper mapper) {
        this.saveAuthService = saveAuthService;
        this.mapper = mapper;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "${kafka.topics.generic-auth-login}")
    public void consume(String message) {
        try {
            AuthLoginEvent event = mapper.toEvent(message);
            log.info("Processing auth login event for email: {}", event.getEmail());

            Auth auth = Auth.builder()
                    .email(event.getEmail())
                    .token(event.getToken())
                    .isAdmin(event.getIsAdmin())
                    .expiresIn(event.getExpiresIn())
                    .createdAt(LocalDateTime.now())
                    .build();

            saveAuthService.save(auth)
                    .subscribe(
                            result -> log.info("Auth saved successfully for email: {}", result.getEmail()),
                            error -> log.error("Error saving auth login: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error processing auth login message: {}", e.getMessage());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthLoginEvent {
        private String email;
        private String token;
        private Boolean isAdmin;
        private Integer expiresIn;
    }

    @Component
    public static class AuthLoginEventMapper {
        public AuthLoginEvent toEvent(String message) throws JsonProcessingException {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(message, AuthLoginEvent.class);
        }
    }
}