package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampCapacityEntity;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BootcampCapacityEntityMapper {
    BootcampCapacity toModel(BootcampCapacityEntity entity);
    BootcampCapacityEntity toEntity(BootcampCapacity model);
}