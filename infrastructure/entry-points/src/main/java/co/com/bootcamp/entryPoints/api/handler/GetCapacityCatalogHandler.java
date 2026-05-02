package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.entryPoints.api.dto.CapacityResponse;
import co.com.bootcamp.entryPoints.api.dto.GenericResponseData;
import co.com.bootcamp.entryPoints.api.mapper.CapacityDTOMapper;
import co.com.bootcamp.usecase.getcapacitycatalog.GetCapacityCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetCapacityCatalogHandler {
    private final GetCapacityCatalogService getService;
    private final CapacityDTOMapper mapper;

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return getService.getAll()
                .map(mapper::toResponse)
                .collectList()
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}