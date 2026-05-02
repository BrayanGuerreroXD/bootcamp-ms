package co.com.bootcamp.usecase.getfullbootcamp;

import co.com.bootcamp.model.bootcamp.*;
import co.com.bootcamp.model.bootcamp.gateways.*;
import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.event.BootcampEvent;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.technologycapacitycatalog.TechnologyCapacityCatalog;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFullBootcampUseCaseTest {

    @Mock
    private BootcampRepository bootcampRepository;
    @Mock
    private BootcampCapacityRepository bootcampCapacityRepository;
    @Mock
    private BootcampPeopleRepository bootcampPeopleRepository;
    @Mock
    private GetTechnologyCapacityCatalogService getTechnologyCapacityCatalogService;
    @Mock
    private EventGateway eventGateway;

    @InjectMocks
    private GetFullBootcampUseCase useCase;

    @Test
    void shouldPublishReportWithAllData() {
        Long bootcampId = 1L;
        Long capacityId = 10L;
        Bootcamp bootcamp = Bootcamp.builder()
                .id(bootcampId)
                .name("Java Bootcamp")
                .description("Learn Java")
                .initTime(LocalDateTime.now())
                .duration(40)
                .build();

        TechnologyCapacityCatalog tech = TechnologyCapacityCatalog.builder()
                .id(100L)
                .name("Java")
                .build();
        CapacityCatalog capacity = CapacityCatalog.builder()
                .id(capacityId)
                .name("Backend")
                .technologies(List.of(tech))
                .build();
        BootcampCapacity bootcampCapacity = BootcampCapacity.builder()
                .bootcamp(bootcamp)
                .capacity(capacity)
                .build();

        BootcampPeople person = BootcampPeople.builder()
                .bootcampId(bootcampId)
                .name("John")
                .email("john@test.com")
                .build();

        when(bootcampRepository.findById(bootcampId)).thenReturn(Mono.just(bootcamp));
        when(bootcampCapacityRepository.findByBootcampId(bootcampId))
                .thenReturn(Flux.just(bootcampCapacity));
        when(getTechnologyCapacityCatalogService.getAllByCapacityId(capacityId))
                .thenReturn(Mono.just(capacity));
        when(bootcampPeopleRepository.findByBootcampId(bootcampId))
                .thenReturn(Flux.just(person));
        when(eventGateway.publishBootcampReportSync(any(BootcampEvent.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.publishReport(bootcampId))
                .verifyComplete();
    }

    @Test
    void shouldGetFullBootcamp() {
        Long bootcampId = 1L;
        Bootcamp bootcamp = Bootcamp.builder()
                .id(bootcampId)
                .name("Java Bootcamp")
                .build();

        when(bootcampRepository.findById(bootcampId)).thenReturn(Mono.just(bootcamp));

        StepVerifier.create(useCase.getFullBootcamp(bootcampId))
                .assertNext(b -> assertThat(b.getName()).isEqualTo("Java Bootcamp"))
                .verifyComplete();
    }
}
