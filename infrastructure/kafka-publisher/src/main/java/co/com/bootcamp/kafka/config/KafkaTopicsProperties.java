package co.com.bootcamp.kafka.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicsProperties {
    private String deleteBootcampMatch;
    private String syncCapacitiesBootcampsMatch;
    private String bootcampReportSync;
}