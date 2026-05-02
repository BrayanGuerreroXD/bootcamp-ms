package co.com.bootcamp.entryPoints.kafka.consumer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TechnologyEventDto {
    private Long id;
    private String name;
}