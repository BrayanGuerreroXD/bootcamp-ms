package co.com.bootcamp.entryPoints.api;

import co.com.bootcamp.entryPoints.api.dto.CapacityResponse;
import co.com.bootcamp.entryPoints.api.handler.GetCapacityCatalogHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CapacityRouter {

    @RouterOperations({
            @RouterOperation(path = "/api/capacities", method = RequestMethod.GET,
                    beanClass = GetCapacityCatalogHandler.class, beanMethod = "getAll",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getAllCapacities",
                            summary = "Get all capacity catalogs",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of capacities",
                                            content = @Content(schema = @Schema(implementation = CapacityResponse.class)))
                            }
                    ))
    })
    @Bean
    public RouterFunction<ServerResponse> capacityRoutes(GetCapacityCatalogHandler getHandler) {
        return route(GET("/api/capacities"), getHandler::getAll);
    }
}