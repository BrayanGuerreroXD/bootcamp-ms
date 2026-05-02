package co.com.bootcamp.usecase.getcapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import co.com.bootcamp.usecase.gettechnologycapacitycatalog.GetTechnologyCapacityCatalogService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetCapacityCatalogUseCase implements GetCapacityCatalogService {
    private final CapacityCatalogRepository capacityCatalogRepository;
    private final GetTechnologyCapacityCatalogService getTechnologyCapacityCatalogService;

    @Override
    public Flux<CapacityCatalog> getAll() {
        return capacityCatalogRepository.findAll()
                .flatMap(capacity -> getTechnologyCapacityCatalogService.getAllByCapacityId(capacity.getId()));
    }
}
