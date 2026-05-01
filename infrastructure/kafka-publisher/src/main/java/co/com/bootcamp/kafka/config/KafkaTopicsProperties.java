package co.com.bootcamp.kafka.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsProperties {
    private String deleteBootcampMatch;
}