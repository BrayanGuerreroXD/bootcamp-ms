package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.TechnologyCapacityCatalogEntity;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TechnologyCapacityCatalogEntityMapper {
    TechnologyCapacityCatalog toModel(TechnologyCapacityCatalogEntity entity);
    TechnologyCapacityCatalogEntity toEntity(TechnologyCapacityCatalog model);
}