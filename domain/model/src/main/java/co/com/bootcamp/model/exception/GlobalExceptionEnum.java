package co.com.bootcamp.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionEnum {
    AUTH_NOT_FOUND("Auth not found", "No auth found with the provided credentials"),
    UNAUTHORIZED("Unauthorized", "Token is missing or invalid"),
    TOKEN_EXPIRED("Token expired", "The provided token has expired"),
    INVALID_REQUEST("Invalid request", "The provided request is invalid"),
    BAD_REQUEST("Bad request", "The provided request is invalid"),
    CONFLICT("Conflict", "A conflict occurred");

    private final String message;
    private final String description;
}