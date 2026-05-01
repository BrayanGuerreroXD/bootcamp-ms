package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.BootcampPeopleEntity;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BootcampPeopleEntityMapper {

    @Mapping(target = "bootcamp", ignore = true)
    @Mapping(target = "bootcampId", source = "entity.bootcampId")
    BootcampPeople toModel(BootcampPeopleEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "bootcamp", ignore = true)
    @Mapping(target = "bootcampId", ignore = true)
    BootcampPeopleEntity toEntity(BootcampPeople model);
}