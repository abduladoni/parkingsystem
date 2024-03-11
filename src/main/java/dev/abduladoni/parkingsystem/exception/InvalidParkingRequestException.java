package dev.abduladoni.parkingsystem.exception;

public class InvalidParkingRequestException extends RuntimeException {
    public InvalidParkingRequestException(String message) {
        super(message);
    }
    public InvalidParkingRequestException(String message, Throwable error) {
        super(message, error);
    }
}
