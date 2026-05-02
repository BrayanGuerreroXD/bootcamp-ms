package co.com.bootcamp.usecase.getfullbootcamp;

import co.com.bootcamp.model.bootcamp.*;
import co.com.bootcamp.model.bootcamp.gateways.*;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.event.*;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class GetFullBootcampUseCase implements GetFullBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;
    private final BootcampPeopleRepository bootcampPeopleRepository;
    private final GetTechnologyCapacityCatalogService getTechnologyCapacityCatalogService;
    private final EventGateway eventGateway;

    @Override
    public Mono<Bootcamp> getFullBootcamp(Long id) {
        return bootcampRepository.findById(id);
    }

    @Override
    public Mono<Void> publishReport(Long bootcampId) {
        return bootcampRepository.findById(bootcampId)
                .flatMap(bootcamp -> bootcampCapacityRepository.findByBootcampId(bootcampId)
                        .collectList()
                        .flatMap(bootcampCapacities -> {
                            Flux<CapacityCatalog> capacitiesFlux = Flux.fromIterable(bootcampCapacities)
                                    .map(BootcampCapacity::getCapacity)
                                    .flatMap(capacity -> getTechnologyCapacityCatalogService.getAllByCapacityId(capacity.getId()));
                            return capacitiesFlux.collectList()
                                    .flatMap(capacities -> bootcampPeopleRepository.findByBootcampId(bootcampId)
                                            .collectList()
                                            .map(people -> buildEvent(bootcamp, capacities, people)));
                        }))
                .flatMap(eventGateway::publishBootcampReportSync);
    }

    private BootcampEvent buildEvent(Bootcamp bootcamp,
                                     List<CapacityCatalog> capacities,
                                     List<BootcampPeople> people) {
        return BootcampEvent.builder()
                .externalId(bootcamp.getId())
                .name(bootcamp.getName())
                .description(bootcamp.getDescription())
                .initTime(bootcamp.getInitTime())
                .duration(bootcamp.getDuration())
                .capacities(capacities.stream()
                        .map(cap -> CapacityEvent.builder()
                                .name(cap.getName())
                                .technologies(cap.getTechnologies() != null
                                        ? cap.getTechnologies().stream()
                                                .map(t -> TechnologyEvent.builder()
                                                        .name(t.getName())
                                                        .build())
                                                .toList()
                                        : List.of())
                                .build())
                        .toList())
                .people(people.stream()
                        .map(p -> PersonEvent.builder()
                                .name(p.getName())
                                .email(p.getEmail())
                                .build())
                        .toList())
                .build();
    }
}
