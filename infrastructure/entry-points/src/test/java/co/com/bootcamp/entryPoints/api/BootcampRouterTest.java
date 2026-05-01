package co.com.bootcamp.entryPoints.api;

import co.com.bootcamp.entryPoints.api.dto.*;
import co.com.bootcamp.entryPoints.api.exception.GlobalExceptionHandler;
import co.com.bootcamp.entryPoints.api.handler.*;
import co.com.bootcamp.entryPoints.api.mapper.BootcampDtoMapper;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.model.bootcamp.BootcampPeople;
import co.com.bootcamp.model.exception.ConflictException;
import co.com.bootcamp.model.exception.GlobalExceptionEnum;
import co.com.bootcamp.model.exception.NotFoundException;
import co.com.bootcamp.usecase.createbootcamp.CreateBootcampService;
import co.com.bootcamp.usecase.deletebootcamp.DeleteBootcampService;
import co.com.bootcamp.usecase.getbootcamp.GetBootcampService;
import co.com.bootcamp.usecase.getbootcamppeople.GetBootcampPeopleService;
import co.com.bootcamp.usecase.signupbootcamp.SignUpBootcampService;
import co.com.bootcamp.usecase.updatebootcamp.UpdateBootcampService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.HttpHandlerConnector;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampRouterTest {

    @Mock private CreateBootcampService createService;
    @Mock private UpdateBootcampService updateService;
    @Mock private GetBootcampService getService;
    @Mock private DeleteBootcampService deleteService;
    @Mock private SignUpBootcampService signUpService;
    @Mock private GetBootcampPeopleService getPeopleService;
    @Mock private BootcampDtoMapper mapper;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        CreateBootcampHandler createHandler = new CreateBootcampHandler(createService, mapper);
        UpdateBootcampHandler updateHandler = new UpdateBootcampHandler(updateService, mapper);
        GetBootcampHandler getHandler = new GetBootcampHandler(getService, mapper);
        DeleteBootcampHandler deleteHandler = new DeleteBootcampHandler(deleteService);
        SignUpBootcampHandler signUpHandler = new SignUpBootcampHandler(signUpService, mapper);
        GetBootcampPeopleHandler peopleHandler = new GetBootcampPeopleHandler(getPeopleService, mapper);

        var routes = new BootcampRouter().bootcampRoutes(
                createHandler, updateHandler, getHandler, deleteHandler, signUpHandler, peopleHandler);
        var exceptionHandler = new GlobalExceptionHandler(new ObjectMapper());

        var httpHandler = WebHttpHandlerBuilder
                .webHandler(RouterFunctions.toWebHandler(routes))
                .exceptionHandler(exceptionHandler)
                .build();

        client = WebTestClient.bindToServer(new HttpHandlerConnector(httpHandler)).build();
    }

    @Test
    void POST_bootcamps_returns201_withBootcampResponse() {
        Bootcamp saved = Bootcamp.builder()
                .id(1L)
                .name("Java Bootcamp")
                .description("Learn Java")
                .build();
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .description("Learn Java")
                .build();

        when(createService.create(any(Bootcamp.class))).thenReturn(Mono.just(saved));
        when(mapper.toResponse(any(Bootcamp.class))).thenReturn(response);

        client.post().uri("/api/bootcamps")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BootcampRequest("Java Bootcamp", "Learn Java", LocalDateTime.now(), 30, List.of(1L)))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Java Bootcamp");
    }

    @Test
    void POST_bootcamps_returns409_whenNameAlreadyExists() {
        when(createService.create(any(Bootcamp.class)))
                .thenReturn(Mono.error(new ConflictException(GlobalExceptionEnum.CONFLICT)));

        client.post().uri("/api/bootcamps")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BootcampRequest("Java Bootcamp", "Learn Java", LocalDateTime.now(), 30, List.of(1L)))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("CONFLICT");
    }

    @Test
    void GET_bootcamps_byId_returns200_withBootcampResponse() {
        Bootcamp found = Bootcamp.builder()
                .id(1L)
                .name("Java Bootcamp")
                .description("Learn Java")
                .build();
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .description("Learn Java")
                .build();

        when(getService.getById(1L)).thenReturn(Mono.just(found));
        when(mapper.toResponse(any(Bootcamp.class))).thenReturn(response);

        client.get().uri("/api/bootcamps/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Java Bootcamp");
    }

    @Test
    void GET_bootcamps_byId_returns404_whenNotFound() {
        when(getService.getById(99L))
                .thenReturn(Mono.error(new NotFoundException(GlobalExceptionEnum.NOT_FOUND)));

        client.get().uri("/api/bootcamps/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("NOT_FOUND");
    }

    @Test
    void GET_bootcamps_returns200_withList() {
        Bootcamp t1 = Bootcamp.builder().id(1L).name("Java Bootcamp").description("Learn Java").build();
        Bootcamp t2 = Bootcamp.builder().id(2L).name("Kotlin Bootcamp").description("Learn Kotlin").build();
        BootcampResponse r1 = BootcampResponse.builder().id(1L).name("Java Bootcamp").description("Learn Java").build();
        BootcampResponse r2 = BootcampResponse.builder().id(2L).name("Kotlin Bootcamp").description("Learn Kotlin").build();

        when(getService.getAll(0, 10, "name", "ASC")).thenReturn(Flux.just(t1, t2));
        when(mapper.toResponse(t1)).thenReturn(r1);
        when(mapper.toResponse(t2)).thenReturn(r2);

        client.get().uri("/api/bootcamps?page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].id").isEqualTo(1)
                .jsonPath("$.data[1].id").isEqualTo(2);
    }

    @Test
    void PUT_bootcamps_byId_returns200_withUpdatedBootcamp() {
        Bootcamp updated = Bootcamp.builder()
                .id(1L)
                .name("Updated Bootcamp")
                .description("Updated description")
                .build();
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Updated Bootcamp")
                .description("Updated description")
                .build();

        when(updateService.update(eq(1L), any(Bootcamp.class))).thenReturn(Mono.just(updated));
        when(mapper.toResponse(any(Bootcamp.class))).thenReturn(response);

        client.put().uri("/api/bootcamps/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BootcampRequest("Updated Bootcamp", "Updated description", LocalDateTime.now(), 45, List.of(1L)))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.name").isEqualTo("Updated Bootcamp");
    }

    @Test
    void PUT_bootcamps_byId_returns404_whenNotFound() {
        when(updateService.update(eq(99L), any(Bootcamp.class)))
                .thenReturn(Mono.error(new NotFoundException(GlobalExceptionEnum.NOT_FOUND)));

        client.put().uri("/api/bootcamps/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BootcampRequest("Java Bootcamp", "Learn Java", LocalDateTime.now(), 30, List.of(1L)))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("NOT_FOUND");
    }

    @Test
    void DELETE_bootcamps_byId_returns204() {
        when(deleteService.delete(1L)).thenReturn(Mono.empty());

        client.delete().uri("/api/bootcamps/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void DELETE_bootcamps_byId_returns404_whenNotFound() {
        when(deleteService.delete(99L))
                .thenReturn(Mono.error(new NotFoundException(GlobalExceptionEnum.NOT_FOUND)));

        client.delete().uri("/api/bootcamps/99")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.data.errorCode").isEqualTo("NOT_FOUND");
    }

    @Test
    void POST_bootcamps_signup_returns201_withBootcampPeopleResponse() {
        BootcampPeople people = BootcampPeople.builder()
                .id(1L)
                .bootcampId(1L)
                .email("test@example.com")
                .build();
        BootcampPeopleResponse response = BootcampPeopleResponse.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        when(signUpService.signUp(eq(1L))).thenReturn(Mono.just(people));
        when(mapper.toResponse(any(BootcampPeople.class))).thenReturn(response);

        client.post().uri("/api/bootcamps/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new SignUpBootcampRequest(1L))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(1)
                .jsonPath("$.data.email").isEqualTo("test@example.com");
    }

    @Test
    void GET_bootcamps_people_returns200_withList() {
        BootcampPeople p1 = BootcampPeople.builder().id(1L).bootcampId(1L).email("test1@example.com").build();
        BootcampPeople p2 = BootcampPeople.builder().id(2L).bootcampId(1L).email("test2@example.com").build();
        BootcampPeopleResponse r1 = BootcampPeopleResponse.builder().id(1L).email("test1@example.com").build();
        BootcampPeopleResponse r2 = BootcampPeopleResponse.builder().id(2L).email("test2@example.com").build();

        when(getPeopleService.getByEmail("test@example.com", 0, 10)).thenReturn(Flux.just(p1, p2));
        when(mapper.toResponse(p1)).thenReturn(r1);
        when(mapper.toResponse(p2)).thenReturn(r2);

        client.get().uri("/api/bootcamps/people?email=test@example.com&page=0&size=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].id").isEqualTo(1)
                .jsonPath("$.data[1].id").isEqualTo(2);
    }
}