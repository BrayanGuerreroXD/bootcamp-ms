package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.entryPoints.api.dto.BootcampRequest;
import co.com.bootcamp.entryPoints.api.dto.GenericResponseData;
import co.com.bootcamp.entryPoints.api.mapper.BootcampDtoMapper;
import co.com.bootcamp.usecase.updatebootcamp.UpdateBootcampService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateBootcampHandler {
    private final UpdateBootcampService updateService;
    private final BootcampDtoMapper mapper;

    public Mono<ServerResponse> handle(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(BootcampRequest.class)
                .map(req -> {
                    var bootcamp = mapper.toDomainUpdate(req);
                    return bootcamp.toBuilder().id(id).build();
                })
                .flatMap(bootcamp -> updateService.update(id, bootcamp))
                .map(mapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}