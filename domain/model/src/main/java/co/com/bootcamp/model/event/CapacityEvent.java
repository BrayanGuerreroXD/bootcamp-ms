package co.com.bootcamp.model.event;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CapacityEvent {
    private String name;
    private List<TechnologyEvent> technologies;
}