package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.CapacityCatalogEntity;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CapacityCatalogEntityMapper {
    CapacityCatalog toModel(CapacityCatalogEntity entity);
    CapacityCatalogEntity toEntity(CapacityCatalog model);
}