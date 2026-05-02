package co.com.bootcamp.drivenadapters.r2dbc.mapper;

import co.com.bootcamp.drivenadapters.r2dbc.entity.AuthEntity;
import co.com.bootcamp.model.auth.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthEntityMapper {
    Auth toModel(AuthEntity entity);
    AuthEntity toEntity(Auth model);
}