package co.com.bootcamp.entryPoints.kafka.consumer.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncCapacityCatalogEventDto {
    private Long id;
    private String name;
    private List<TechnologyEventDto> technologies;
}