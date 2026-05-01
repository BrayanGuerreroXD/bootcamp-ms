package co.com.bootcamp.drivenAdapters.r2dbc.mapper;

import co.com.bootcamp.drivenAdapters.r2dbc.entity.TechnologyCapacityCatalogEntity;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechnologyCapacityCatalogEntityMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TechnologyCapacityCatalog toModel(TechnologyCapacityCatalogEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TechnologyCapacityCatalogEntity toEntity(TechnologyCapacityCatalog model);
}