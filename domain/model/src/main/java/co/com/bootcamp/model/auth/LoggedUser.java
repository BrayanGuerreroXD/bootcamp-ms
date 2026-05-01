package co.com.bootcamp.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoggedUser {
    private String email;
    private Boolean isAdmin;
}