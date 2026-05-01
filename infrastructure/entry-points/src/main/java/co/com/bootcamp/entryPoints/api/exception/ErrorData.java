package co.com.bootcamp.entryPoints.api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorData {
    private final String errorCode;
    private final String message;
    private final String description;

    public static ErrorData of(co.com.bootcamp.model.exception.GlobalExceptionEnum error) {
        return new ErrorData(error.name(), error.getMessage(), error.getDescription());
    }
}