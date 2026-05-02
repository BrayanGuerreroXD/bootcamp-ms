package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLoginEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.mapper.AuthLoginEventMapper;
import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.usecase.saveauth.SaveAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthLoginConsumer {
    private final SaveAuthService saveAuthService;
    private final AuthLoginEventMapper mapper;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.generic-auth-login}")
    public void consume(String message) {
        try {
            AuthLoginEventDto dto = objectMapper.readValue(message, AuthLoginEventDto.class);
            log.info("Processing auth login event for email: {}", dto.getEmail());

            Auth auth = mapper.toModel(dto);

            saveAuthService.save(auth)
                    .subscribe(
                            result -> log.info("Auth saved successfully for email: {}", result.getEmail()),
                            error -> log.error("Error saving auth login: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error processing auth login message: {}", e.getMessage());
        }
    }
}