package co.com.bootcamp.model.event;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BootcampDeleteMatchEvent {
    private Long bootcampId;
    private List<Long> capacityIds;
}