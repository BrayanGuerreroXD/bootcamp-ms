package co.com.bootcamp.entryPoints.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SignUpBootcampRequest {
    @NotNull(message = "Bootcamp ID is required")
    private Long bootcampId;

    private String email;
}