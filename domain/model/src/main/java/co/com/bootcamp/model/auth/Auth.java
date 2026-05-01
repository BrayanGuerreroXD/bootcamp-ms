package co.com.bootcamp.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Auth {
    private Long id;
    private String email;
    private String token;
    private Boolean isAdmin;
    private Integer expiresIn;
    private LocalDateTime createdAt;
}