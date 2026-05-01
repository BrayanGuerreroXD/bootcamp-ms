package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.entryPoints.api.dto.GenericResponseData;
import co.com.bootcamp.entryPoints.api.mapper.BootcampDtoMapper;
import co.com.bootcamp.usecase.getbootcamp.GetBootcampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetBootcampHandler {
    private final GetBootcampService getService;
    private final BootcampDtoMapper mapper;

    public Mono<ServerResponse> getById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return getService.getById(id)
                .map(mapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String direction = request.queryParam("direction").orElse("ASC");

        return getService.getAll(page, size, sortBy, direction)
                .map(mapper::toResponse)
                .collectList()
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}