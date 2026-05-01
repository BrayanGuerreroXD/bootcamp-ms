package co.com.bootcamp.entryPoints.api.config;

import co.com.bootcamp.entryPoints.api.security.AuthSecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthSecurityContextRepository authSecurityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(org.springframework.security.config.web.server.ServerHttpSecurity http) {
        return http
                .csrf(org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(authSecurityContextRepository)
                .authorizeExchange(auth -> auth
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/swagger-ui.html").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                        .accessDeniedHandler((exchange, e) ->
                                Mono.fromRunnable(() ->
                                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
                )
                .build();
    }
}