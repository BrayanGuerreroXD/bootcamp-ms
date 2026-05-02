package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLoginEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.mapper.AuthLoginEventMapper;
import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.usecase.saveauth.SaveAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLoginConsumerTest {

    @Mock
    private SaveAuthService saveAuthService;

    @Mock
    private AuthLoginEventMapper mapper;

    private ObjectMapper objectMapper;
    private AuthLoginConsumer consumer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        consumer = new AuthLoginConsumer(saveAuthService, mapper, objectMapper);
    }

    @Test
    void consume_validMessage_savesAuth() {
        String message = """
            {
                "email": "test@example.com",
                "token": "token123",
                "isAdmin": true,
                "expiresIn": 3600
            }
            """;

        AuthLoginEventDto dto = AuthLoginEventDto.builder()
                .email("test@example.com")
                .token("token123")
                .isAdmin(true)
                .expiresIn(3600)
                .build();

        Auth savedAuth = Auth.builder()
                .id(1L)
                .email("test@example.com")
                .token("token123")
                .isAdmin(true)
                .expiresIn(3600)
                .build();

        when(mapper.toModel(any(AuthLoginEventDto.class))).thenReturn(savedAuth);
        when(saveAuthService.save(any())).thenReturn(Mono.just(savedAuth));

        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0, "key", message);
        consumer.consume(record);

        verify(saveAuthService).save(any());
    }
}