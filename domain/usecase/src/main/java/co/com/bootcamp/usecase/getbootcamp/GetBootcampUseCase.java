package co.com.bootcamp.usecase.getbootcamp;

import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampCapacity;

import co.com.bootcamp.model.bootcamp.gateways.BootcampCapacityRepository;
import co.com.bootcamp.model.bootcamp.gateways.BootcampRepository;
import co.com.bootcamp.model.bootcamp.gateways.OrderDirection;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetBootcampUseCase implements GetBootcampService {
    private final BootcampRepository bootcampRepository;
    private final BootcampCapacityRepository bootcampCapacityRepository;

    @Override
    public Mono<Bootcamp> getById(Long id) {
        return bootcampRepository.findById(id)
                .flatMap(this::enrichWithCapacities);
    }

    @Override
    public Flux<Bootcamp> getAll(int page, int size, String sortBy, String direction) {
        OrderDirection orderDir = "DESC".equalsIgnoreCase(direction)
            ? OrderDirection.DESC : OrderDirection.ASC;

        return bootcampRepository.findAllOrderByName(orderDir)
                .skip(page * size)
                .take(size)
                .flatMap(this::enrichWithCapacities);
    }

    private Mono<Bootcamp> enrichWithCapacities(Bootcamp bootcamp) {
        return bootcampCapacityRepository.findByBootcampId(bootcamp.getId())
                .map(BootcampCapacity::getCapacity)
                .collectList()
                .map(capacities -> bootcamp.toBuilder()
                        .capacities(capacities)
                        .capacityCount(capacities.size())
                        .build());
    }
}