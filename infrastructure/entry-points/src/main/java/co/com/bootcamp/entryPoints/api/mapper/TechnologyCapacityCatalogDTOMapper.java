package co.com.bootcamp.entryPoints.api.mapper;

import co.com.bootcamp.entryPoints.api.dto.TechnologyResponse;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechnologyCapacityCatalogDTOMapper {
    TechnologyResponse toResponse(TechnologyCapacityCatalog technology);
}
