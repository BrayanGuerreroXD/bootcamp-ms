package co.com.bootcamp.entryPoints.kafka.consumer;

import co.com.bootcamp.usecase.deleteauth.DeleteAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthLogoutConsumerTest {

    @Mock
    private DeleteAuthService deleteAuthService;

    private AuthLogoutConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new AuthLogoutConsumer(deleteAuthService, new ObjectMapper());
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

        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0, "key", message);
        consumer.consume(record);

        verify(deleteAuthService).deleteByEmailAndToken("test@example.com", "token123");
    }
}