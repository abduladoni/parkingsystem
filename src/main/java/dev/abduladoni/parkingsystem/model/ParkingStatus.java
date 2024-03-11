package dev.abduladoni.parkingsystem.model;
public enum ParkingStatus {
    ACTIVE("Active"),
    ENDED("Ended");

    private final String status;

    ParkingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
