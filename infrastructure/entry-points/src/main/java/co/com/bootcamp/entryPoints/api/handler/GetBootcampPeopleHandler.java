package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.drivenAdapters.r2dbc.mapper.BootcampDtoMapper;
import co.com.bootcamp.entryPoints.api.exception.GlobalExceptionHandler.GenericResponseData;
import co.com.bootcamp.usecase.getbootcamppeople.GetBootcampPeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetBootcampPeopleHandler {
    private final GetBootcampPeopleService getService;
    private final BootcampDtoMapper mapper;

    public Mono<ServerResponse> getByEmail(ServerRequest request) {
        String email = request.queryParam("email").orElse("");
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        return getService.getByEmail(email, page, size)
                .map(mapper::toResponse)
                .collectList()
                .map(GenericResponseData::of)
                .flatMap(body -> ServerResponse.ok().bodyValue(body));
    }
}