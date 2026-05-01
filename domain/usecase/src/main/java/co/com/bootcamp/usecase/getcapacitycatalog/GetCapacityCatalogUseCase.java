package co.com.bootcamp.usecase.getcapacitycatalog;

import co.com.bootcamp.model.capacitycatalog.CapacityCatalog;
import co.com.bootcamp.model.capacitycatalog.gateways.CapacityCatalogRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetCapacityCatalogUseCase implements GetCapacityCatalogService {
    private final CapacityCatalogRepository capacityCatalogRepository;

    @Override
    public Flux<CapacityCatalog> getAll() {
        return capacityCatalogRepository.findAll();
    }
}
