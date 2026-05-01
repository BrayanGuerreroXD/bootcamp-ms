package co.com.bootcamp.entryPoints.api.handler;

import co.com.bootcamp.usecase.deletebootcamp.DeleteBootcampService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteBootcampHandler {
    private final DeleteBootcampService deleteService;

    public Mono<ServerResponse> handle(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return deleteService.delete(id)
                .thenReturn(HttpStatus.NO_CONTENT)
                .flatMap(status -> ServerResponse.status(status).build());
    }
}