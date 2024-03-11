package dev.abduladoni.parkingsystem.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParkingSessionDTO {
        private String vehicleNumber;
        private String streetName;
        private String entryTime;
        private String exitTime;
        private String parkingFee;
        private String status;
}
