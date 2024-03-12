package dev.abduladoni.parkingsystem.exception;

public class InvalidParkingRequestException extends RuntimeException {
    public InvalidParkingRequestException(String message) {
        super(message);
    }
}
