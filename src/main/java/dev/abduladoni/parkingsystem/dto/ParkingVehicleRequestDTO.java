package dev.abduladoni.parkingsystem.dto;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Builder
public class ParkingVehicleRequestDTO {
    @NotBlank(message = "Vehicle number cannot be blank")
    private String vehicleNumber;

    @NotBlank(message = "Street name cannot be blank")
    private String streetName;
}