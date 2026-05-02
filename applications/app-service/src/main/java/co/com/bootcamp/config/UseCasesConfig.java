package co.com.bootcamp.config;

import co.com.bootcamp.usecase.getfullbootcamp.GetFullBootcampService;
import co.com.bootcamp.usecase.getfullbootcamp.GetFullBootcampUseCase;
import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampPeopleRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.model.event.gateways.EventGateway;
import co.com.bootcamp.model.technologycapacitycatalog.gateways.TechnologyCapacityCatalogRepository;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogService;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bootcamp.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    public GetFullBootcampService getFullBootcampService(
            BootcampRepository bootcampRepository,
            BootcampCapacityRepository bootcampCapacityRepository,
            BootcampPeopleRepository bootcampPeopleRepository,
            GetTechnologyCapacityCatalogService getTechnologyCapacityCatalogService,
            EventGateway eventGateway) {
        return new GetFullBootcampUseCase(
                bootcampRepository,
                bootcampCapacityRepository,
                bootcampPeopleRepository,
                getTechnologyCapacityCatalogService,
                eventGateway);
    }
}
