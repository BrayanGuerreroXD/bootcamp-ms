package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.usecase.deleteauth.DeleteAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLogoutConsumerTest {

    @Mock
    private DeleteAuthService deleteAuthService;

    private AuthLogoutConsumer.AuthLogoutEventMapper mapper;
    private AuthLogoutConsumer consumer;

    @BeforeEach
    void setUp() {
        mapper = new AuthLogoutConsumer.AuthLogoutEventMapper();
        consumer = new AuthLogoutConsumer(deleteAuthService, mapper);
    }

    @Test
    void consume_validMessage_deletesAuth() {
        String message = """
            {
                "email": "test@example.com",
                "token": "token123"
            }
            """;

        when(deleteAuthService.deleteByEmailAndToken("test@example.com", "token123"))
                .thenReturn(Mono.empty());

        consumer.consume(message);

        verify(deleteAuthService).deleteByEmailAndToken("test@example.com", "token123");
    }
}