package co.com.bootcamp.model.exception;

public enum GlobalExceptionEnum {
    AUTH_NOT_FOUND("Auth not found", "No auth found with the provided credentials"),
    UNAUTHORIZED("Unauthorized", "Token is missing or invalid"),
    TOKEN_EXPIRED("Token expired", "The provided token has expired"),
    INVALID_REQUEST("Invalid request", "The provided request is invalid"),
    BAD_REQUEST("Bad request", "The provided request is invalid"),
    CONFLICT("Conflict", "A conflict occurred"),
    FORBIDDEN_ACCESS("Forbidden access", "You do not have permission to access this resource"),
    NOT_FOUND("Not found", "The requested resource was not found"),
    MAX_BOOTCAMPS_ENROLLED("Max bootcamps enrolled", "Cannot enroll in more than 5 bootcamps"),
    ALREADY_ENROLLED("Already enrolled", "User is already enrolled in this bootcamp");

    private final String message;
    private final String description;

    GlobalExceptionEnum(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}