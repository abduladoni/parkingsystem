package dev.abduladoni.parkingsystem.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiErrorResponse {
    private String message;
    private int status;
    private List<String> errors;
    private LocalDateTime timestamp;

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(error -> this.errors.add(error.getField() + ": " + error.getDefaultMessage()));
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(error -> this.errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));
    }
}
