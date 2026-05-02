package co.com.bootcamp.entryPoints.api;

import co.com.bootcamp.entryPoints.api.dto.BootcampRequest;
import co.com.bootcamp.entryPoints.api.dto.BootcampResponse;
import co.com.bootcamp.entryPoints.api.dto.BootcampPeopleResponse;
import co.com.bootcamp.entryPoints.api.dto.SignUpBootcampRequest;
import co.com.bootcamp.entryPoints.api.handler.CreateBootcampHandler;
import co.com.bootcamp.entryPoints.api.handler.UpdateBootcampHandler;
import co.com.bootcamp.entryPoints.api.handler.GetBootcampHandler;
import co.com.bootcamp.entryPoints.api.handler.DeleteBootcampHandler;
import co.com.bootcamp.entryPoints.api.handler.SignUpBootcampHandler;
import co.com.bootcamp.entryPoints.api.handler.GetBootcampPeopleHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {

    @RouterOperations({
            @RouterOperation(path = "/api/bootcamps", method = RequestMethod.POST,
                    beanClass = CreateBootcampHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "createBootcamp",
                            summary = "Create a new bootcamp",
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = BootcampRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Bootcamp created successfully",
                                            content = @Content(schema = @Schema(implementation = BootcampResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "404", description = "Capacity not found")
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.PUT,
                    beanClass = UpdateBootcampHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "updateBootcamp",
                            summary = "Update an existing bootcamp",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = BootcampRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Bootcamp updated successfully",
                                            content = @Content(schema = @Schema(implementation = BootcampResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "404", description = "Bootcamp not found")
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.GET,
                    beanClass = GetBootcampHandler.class, beanMethod = "getById",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getBootcampById",
                            summary = "Get bootcamp by ID",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Bootcamp found",
                                            content = @Content(schema = @Schema(implementation = BootcampResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "Bootcamp not found")
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps", method = RequestMethod.GET,
                    beanClass = GetBootcampHandler.class, beanMethod = "getAll",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getAllBootcamps",
                            summary = "Get all bootcamps with pagination and sorting",
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "integer", defaultValue = "10")),
                                    @Parameter(name = "sortBy", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "string", defaultValue = "name")),
                                    @Parameter(name = "direction", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "string", defaultValue = "ASC"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of bootcamps",
                                            content = @Content(schema = @Schema(implementation = BootcampResponse.class)))
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.DELETE,
                    beanClass = DeleteBootcampHandler.class, beanMethod = "handle",
                    operation = @Operation(
                            operationId = "deleteBootcamp",
                            summary = "Delete a bootcamp by ID",
                            parameters = {
                                    @Parameter(name = "id", in = ParameterIn.PATH, required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "204", description = "Bootcamp deleted successfully"),
                                    @ApiResponse(responseCode = "404", description = "Bootcamp not found")
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps/signup", method = RequestMethod.POST,
                    beanClass = SignUpBootcampHandler.class, beanMethod = "handle",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "signUpBootcamp",
                            summary = "Sign up for a bootcamp",
                            requestBody = @RequestBody(required = true,
                                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = SignUpBootcampRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Sign up successful",
                                            content = @Content(schema = @Schema(implementation = BootcampResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                                    @ApiResponse(responseCode = "404", description = "Bootcamp not found"),
                                    @ApiResponse(responseCode = "409", description = "Already registered in this bootcamp")
                            }
                    )),
            @RouterOperation(path = "/api/bootcamps/people", method = RequestMethod.GET,
                    beanClass = GetBootcampPeopleHandler.class, beanMethod = "getByEmail",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    operation = @Operation(
                            operationId = "getBootcampPeopleByEmail",
                            summary = "Get bootcamp participants by email",
                            parameters = {
                                    @Parameter(name = "email", in = ParameterIn.QUERY, required = true,
                                            schema = @Schema(type = "string")),
                                    @Parameter(name = "page", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "integer", defaultValue = "0")),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, required = false,
                                            schema = @Schema(type = "integer", defaultValue = "10"))
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "List of bootcamp participants",
                                            content = @Content(schema = @Schema(implementation = BootcampPeopleResponse.class))),
                                    @ApiResponse(responseCode = "404", description = "No participants found")
                            }
                    ))
    })
    @Bean
    public RouterFunction<ServerResponse> bootcampRoutes(
            CreateBootcampHandler createHandler,
            UpdateBootcampHandler updateHandler,
            GetBootcampHandler getHandler,
            DeleteBootcampHandler deleteHandler,
            SignUpBootcampHandler signUpHandler,
            GetBootcampPeopleHandler peopleHandler) {
        return route(POST("/api/bootcamps"), createHandler::handle)
                .andRoute(GET("/api/bootcamps/{id}"), getHandler::getById)
                .andRoute(GET("/api/bootcamps"), getHandler::getAll)
                .andRoute(PUT("/api/bootcamps/{id}"), updateHandler::handle)
                .andRoute(DELETE("/api/bootcamps/{id}"), deleteHandler::handle)
                .andRoute(POST("/api/bootcamps/signup"), signUpHandler::handle)
                .andRoute(GET("/api/bootcamps/people"), peopleHandler::getByEmail);
    }
}