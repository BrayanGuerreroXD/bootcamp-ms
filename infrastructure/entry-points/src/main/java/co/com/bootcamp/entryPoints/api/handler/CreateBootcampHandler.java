package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.entryPoints.api.dto.BootcampRequest;
import co.com.bootcamp.entryPoints.api.dto.GenericResponseData;
import co.com.bootcamp.entryPoints.api.mapper.BootcampDtoMapper;
import co.com.bootcamp.usecase.createbootcamp.CreateBootcampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateBootcampHandler {
    private final CreateBootcampService createService;
    private final BootcampDtoMapper mapper;

    public Mono<ServerResponse> handle(ServerRequest request) {
        return request.bodyToMono(BootcampRequest.class)
                .map(mapper::toDomain)
                .flatMap(createService::create)
                .map(mapper::toResponse)
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.status(HttpStatus.CREATED).bodyValue(body));
    }
}