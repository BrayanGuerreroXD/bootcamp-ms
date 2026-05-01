package co.com.bootcamp.entryPoints.api;

import co.com.bootcamp.drivenAdapters.r2dbc.mapper.BootcampDtoMapper;
import co.com.bootcamp.entryPoints.api.handler.*;
import co.com.bootcamp.model.bootcamp.Bootcamp;
import co.com.bootcamp.entryPoints.api.dto.BootcampRequest;
import co.com.bootcamp.entryPoints.api.dto.BootcampResponse;
import co.com.bootcamp.entryPoints.api.dto.GenericResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest
@Import({BootcampRouter.class, CreateBootcampHandler.class, UpdateBootcampHandler.class,
         GetBootcampHandler.class, DeleteBootcampHandler.class, SignUpBootcampHandler.class,
         GetBootcampPeopleHandler.class})
class BootcampRouterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BootcampDtoMapper mapper;

    @MockBean
    private CreateBootcampHandler createBootcampHandler;

    @MockBean
    private UpdateBootcampHandler updateBootcampHandler;

    @MockBean
    private GetBootcampHandler getBootcampHandler;

    @MockBean
    private DeleteBootcampHandler deleteBootcampHandler;

    @MockBean
    private SignUpBootcampHandler signUpBootcampHandler;

    @MockBean
    private GetBootcampPeopleHandler getBootcampPeopleHandler;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createBootcamp_ShouldReturnCreated() {
        BootcampRequest request = BootcampRequest.builder()
                .name("Java Bootcamp")
                .description("Learn Java")
                .initTime(LocalDateTime.now())
                .duration(30)
                .capacities(List.of(1L, 2L))
                .build();

        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .description("Learn Java")
                .build();

        when(createBootcampHandler.handle(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.status(org.springframework.http.HttpStatus.CREATED)
                        .bodyValue(GenericResponseData.of(response)))
        );

        webTestClient.post()
                .uri("/api/bootcamps")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void getBootcampById_ShouldReturnOk() {
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .build();

        when(getBootcampHandler.getById(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.ok()
                        .bodyValue(GenericResponseData.of(response)))
        );

        webTestClient.get()
                .uri("/api/bootcamps/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getAllBootcamps_ShouldReturnOk() {
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .build();

        when(getBootcampHandler.getAll(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.ok()
                        .bodyValue(GenericResponseData.of(List.of(response))))
        );

        webTestClient.get()
                .uri("/api/bootcamps")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateBootcamp_ShouldReturnOk() {
        BootcampRequest request = BootcampRequest.builder()
                .name("Updated Bootcamp")
                .description("Updated description")
                .initTime(LocalDateTime.now())
                .duration(45)
                .capacities(List.of(1L))
                .build();

        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Updated Bootcamp")
                .build();

        when(updateBootcampHandler.handle(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.ok()
                        .bodyValue(GenericResponseData.of(response)))
        );

        webTestClient.put()
                .uri("/api/bootcamps/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void deleteBootcamp_ShouldReturnNoContent() {
        when(deleteBootcampHandler.handle(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.noContent().build())
        );

        webTestClient.delete()
                .uri("/api/bootcamps/1")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void signUpBootcamp_ShouldReturnCreated() {
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .build();

        when(signUpBootcampHandler.handle(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.status(org.springframework.http.HttpStatus.CREATED)
                        .bodyValue(GenericResponseData.of(response)))
        );

        webTestClient.post()
                .uri("/api/bootcamps/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"bootcampId\": 1, \"email\": \"test@example.com\"}")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void getBootcampPeople_ShouldReturnOk() {
        BootcampResponse response = BootcampResponse.builder()
                .id(1L)
                .name("Java Bootcamp")
                .build();

        when(getBootcampPeopleHandler.getByEmail(any())).thenReturn(
                Mono.just(org.springframework.http.server.reactive.ServerResponse.ok()
                        .bodyValue(GenericResponseData.of(List.of(response))))
        );

        webTestClient.get()
                .uri("/api/bootcamps/people?email=test@example.com")
                .exchange()
                .expectStatus().isOk();
    }
}