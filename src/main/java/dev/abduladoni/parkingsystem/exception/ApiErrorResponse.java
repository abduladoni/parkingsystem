package dev.abduladoni.parkingsystem.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiErrorResponse {
    private String message;
    private int status;
    private List<String> errors;
    private LocalDateTime timestamp;
}
