package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.model.auth.Auth;
import co.com.bootcamp.usecase.saveauth.SaveAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLoginConsumerTest {

    @Mock
    private SaveAuthService saveAuthService;

    private AuthLoginConsumer.AuthLoginEventMapper mapper;
    private AuthLoginConsumer consumer;

    @BeforeEach
    void setUp() {
        mapper = new AuthLoginConsumer.AuthLoginEventMapper();
        consumer = new AuthLoginConsumer(saveAuthService, mapper);
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

        Auth savedAuth = Auth.builder()
                .id(1L)
                .email("test@example.com")
                .token("token123")
                .isAdmin(true)
                .expiresIn(3600)
                .build();

        when(saveAuthService.save(any())).thenReturn(Mono.just(savedAuth));

        consumer.consume(message);

        verify(saveAuthService).save(any());
    }
}