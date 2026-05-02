package co.com.bootcamp.entryPoints.kafka.consumer.mapper;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLogoutEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class AuthLogoutEventMapper {
    private final ObjectMapper objectMapper;

    public AuthLogoutEventDto toDto(String message) throws Exception {
        return objectMapper.readValue(message, AuthLogoutEventDto.class);
    }
}