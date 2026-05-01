package co.com.bootcamp.entryPoints.kafka.consumer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginEventDto {
    private String email;
    private String token;
    private Boolean isAdmin;
    private Integer expiresIn;
}