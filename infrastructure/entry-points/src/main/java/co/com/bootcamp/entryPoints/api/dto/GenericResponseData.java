package co.com.bootcamp.entryPoints.api.dto;

import co.com.bootcamp.entryPoints.api.exception.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GenericResponseData<T> {
    private T data;

    public static <T> GenericResponseData<T> of(T data) {
        return GenericResponseData.<T>builder().data(data).build();
    }
}