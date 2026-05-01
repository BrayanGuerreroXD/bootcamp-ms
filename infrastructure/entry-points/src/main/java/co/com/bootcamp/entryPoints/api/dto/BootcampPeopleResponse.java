package co.com.bootcamp.entryPoints.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BootcampPeopleResponse {
    private Long id;
    private String email;
    private BootcampResponse bootcamp;
}