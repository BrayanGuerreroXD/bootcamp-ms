package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampEntity;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BootcampEntityMapper {
    @Mapping(target = "capacities", ignore = true)
    Bootcamp toModel(BootcampEntity entity);
    BootcampEntity toEntity(Bootcamp model);
}