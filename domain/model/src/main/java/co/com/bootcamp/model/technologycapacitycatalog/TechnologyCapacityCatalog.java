package co.com.bootcamp.model.technologycapacitycatalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class TechnologyCapacityCatalog {
    private Long id;
    private Long externalId;
    private String name;
}
