package co.com.bootcamp.model.bootcamp;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class BootcampCapacity {
    private Long id;
    private Bootcamp bootcamp;
    private CapacityCatalog capacity;
}