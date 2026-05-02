package co.com.bootcamp.entryPoints.kafka.consumer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLogoutEventDto {
    private String email;
    private String token;
}