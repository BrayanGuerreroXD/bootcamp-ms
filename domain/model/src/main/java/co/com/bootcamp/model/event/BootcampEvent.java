package co.com.bootcamp.model.event;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BootcampEvent {
    private Long externalId;
    private String name;
    private String description;
    private LocalDateTime initTime;
    private Integer duration;
    private List<CapacityEvent> capacities;
    private List<PersonEvent> people;
}