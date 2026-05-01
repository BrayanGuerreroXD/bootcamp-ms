package co.com.bootcamp.entryPoints.api;

import co.com.bootcamp.entryPoints.api.handler.*;
import io.swagger.v3.oas.annotations.Operation;
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
                    beanClass = CreateBootcampHandler.class, beanMethod = "handle"),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.PUT,
                    beanClass = UpdateBootcampHandler.class, beanMethod = "handle"),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.GET,
                    beanClass = GetBootcampHandler.class, beanMethod = "getById"),
            @RouterOperation(path = "/api/bootcamps", method = RequestMethod.GET,
                    beanClass = GetBootcampHandler.class, beanMethod = "getAll"),
            @RouterOperation(path = "/api/bootcamps/{id}", method = RequestMethod.DELETE,
                    beanClass = DeleteBootcampHandler.class, beanMethod = "handle"),
            @RouterOperation(path = "/api/bootcamps/signup", method = RequestMethod.POST,
                    beanClass = SignUpBootcampHandler.class, beanMethod = "handle"),
            @RouterOperation(path = "/api/bootcamps/people", method = RequestMethod.GET,
                    beanClass = GetBootcampPeopleHandler.class, beanMethod = "getByEmail")
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