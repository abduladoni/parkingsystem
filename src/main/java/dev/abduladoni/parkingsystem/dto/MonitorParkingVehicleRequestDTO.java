package dev.abduladoni.parkingsystem.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Builder
public class MonitorParkingVehicleRequestDTO{

    @NotBlank(message = "Vehicle number cannot be blank")
    @Size(max = 10, message = "Vehicle number must be less than or equal to 10 characters")
    @Pattern(regexp = "^(?:[A-Z0-9-]{6,7}|[A-Z0-9-]{9}|[A-Z0-9-]{10}|[A-Z]{2}-[0-9]{2}-[0-9]{2})$",
            message = "Invalid vehicle number format")
    private String vehicleNumber;

    @NotBlank(message = "Street name cannot be blank")
    @Size(max = 50, message = "Street name must be less than or equal to 50 characters")
    private String streetName;

    @NotBlank(message = "Observation time cannot be blank")
    private String observationTime;

}
