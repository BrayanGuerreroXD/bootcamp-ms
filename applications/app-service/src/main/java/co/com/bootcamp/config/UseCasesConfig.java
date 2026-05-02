package co.com.bootcamp.config;

import co.com.bootcamp.usecase.getfullbootcamp.GetFullBootcampService;
import co.com.bootcamp.usecase.getfullbootcamp.GetFullBootcampUseCase;
import co.com.bootcamp.repository.bootcamp.BootcampCapacityRepository;
import co.com.bootcamp.repository.bootcamp.BootcampPeopleRepository;
import co.com.bootcamp.repository.bootcamp.BootcampRepository;
import co.com.bootcamp.gateway.EventGateway;
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
            EventGateway eventGateway) {
        return new GetFullBootcampUseCase(
                bootcampRepository,
                bootcampCapacityRepository,
                bootcampPeopleRepository,
                eventGateway);
    }
}
