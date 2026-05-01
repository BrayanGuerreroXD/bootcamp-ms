package co.com.bootcamp.entryPoints.api.mapper;

import co.com.bootcamp.entryPoints.api.dto.*;
import co.com.bootcamp.model.bootcamp.*;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        CapacityDTOMapper.class,
})
public interface BootcampDtoMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capacityCount", ignore = true)
    @Mapping(target = "capacities", ignore = true)
    Bootcamp toDomain(BootcampRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "capacityCount", ignore = true)
    @Mapping(target = "capacities", ignore = true)
    Bootcamp toDomainUpdate(BootcampRequest request);

    @Mapping(target = "capacities", source = "capacities")
    BootcampResponse toResponse(Bootcamp bootcamp);
    
//    @Mapping(target = "capacities", source = "capacities", qualifiedByName = "capacitiesToResponse")
//    BootcampResponse toResponse(Bootcamp bootcamp);
    
    BootcampPeopleResponse toResponse(BootcampPeople people);
    
//    @Named("capacitiesToResponse")
//    default List<CapacityResponse> capacitiesToResponse(List<BootcampCapacity> capacities) {
//        if (capacities == null) return null;
//        return capacities.stream()
//                .map(bc -> {
//                    CapacityCatalog cap = bc.getCapacity();
//                    if (cap == null) return null;
//                    List<TechnologyResponse> techs = cap.getTechnologies() != null
//                        ? cap.getTechnologies().stream()
//                            .map(tech -> TechnologyResponse.builder()
//                                    .id(tech.getId())
//                                    .name(tech.getName())
//                                    .build())
//                            .toList()
//                        : null;
//                    return CapacityResponse.builder()
//                            .id(cap.getId())
//                            .name(cap.getName())
//                            .technologies(techs)
//                            .build();
//                })
//                .toList();
//    }
}