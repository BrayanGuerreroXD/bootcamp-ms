package co.com.bootcamp.entryPoints.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BootcampRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Init time is required")
    private LocalDateTime initTime;

    @NotNull(message = "Duration is required")
    private Integer duration;

    @NotEmpty(message = "At least one capacity is required")
    @Size(min = 1, max = 4, message = "Must have between 1 and 4 capacities")
    private List<Long> capacities;
}