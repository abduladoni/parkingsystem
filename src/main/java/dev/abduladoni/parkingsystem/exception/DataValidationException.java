package dev.abduladoni.parkingsystem.exception;

public class DataValidationException extends RuntimeException {
    public DataValidationException(String message, Throwable error) {
        super(message, error);
    }
}
