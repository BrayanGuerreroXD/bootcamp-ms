package co.com.bootcamp.entryPoints.kafka.consumer.mapper;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.AuthLoginEventDto;
import co.com.bootcamp.model.auth.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthLoginEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Auth toModel(AuthLoginEventDto dto);
}