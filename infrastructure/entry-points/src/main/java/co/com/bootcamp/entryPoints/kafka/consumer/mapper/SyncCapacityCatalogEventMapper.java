package co.com.bootcamp.entryPoints.kafka.consumer.mapper;

import co.com.bootcamp.entryPoints.kafka.consumer.dto.SyncCapacityCatalogEventDto;
import co.com.bootcamp.entryPoints.kafka.consumer.dto.TechnologyEventDto;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SyncCapacityCatalogEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "technologies", source = "technologies")
    CapacityCatalog toModel(SyncCapacityCatalogEventDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capacityCatalogId", ignore = true)
    @Mapping(target = "externalId", source = "id")
    TechnologyCapacityCatalog toTechnologyModel(TechnologyEventDto dto);
}