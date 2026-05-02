package co.com.bootcamp.model.event;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CapacityBootcampSyncEvent {
    private Long bootcampId;
    private List<Long> capacityIds;
}