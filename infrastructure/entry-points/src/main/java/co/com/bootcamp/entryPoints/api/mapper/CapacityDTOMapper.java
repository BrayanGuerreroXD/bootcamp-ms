package co.com.bootcamp.entryPoints.api.mapper;

import co.com.bootcamp.entryPoints.api.dto.CapacityResponse;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        TechnologyCapacityCatalogDTOMapper.class
})
public interface CapacityDTOMapper {
    CapacityResponse toResponse(CapacityCatalog capacity);
}
