package co.com.bootcamp.model.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private final GlobalExceptionEnum error;

    public ForbiddenException(GlobalExceptionEnum error) {
        super(error.getMessage());
        this.error = error;
    }
}