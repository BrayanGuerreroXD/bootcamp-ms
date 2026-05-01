package co.com.bootcamp.entryPoints.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TechnologyResponse {
    private Long id;
    private String name;
}