package co.com.bootcamp.model.bootcamp;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Bootcamp {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime initTime;
    private Integer duration;
    private Integer capacityCount;
    private List<CapacityCatalog> capacities;
}