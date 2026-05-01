package co.com.bootcamp.model.capacitycatalog;

import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class CapacityCatalog {
    private Long id;
    private Long externalId;
    private String name;
    private List<TechnologyCapacityCatalog> technologies;
}
