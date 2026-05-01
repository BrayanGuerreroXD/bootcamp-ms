package co.com.bootcamp.entryPoints.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CapacityResponse {
    private Long id;
    private String name;
    private List<TechnologyResponse> technologies;
}