package dev.abduladoni.parkingsystem.controller;

import dev.abduladoni.parkingsystem.dto.MonitorParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.dto.ParkingSessionDTO;
import dev.abduladoni.parkingsystem.dto.ResponseDTO;
import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.exception.ApiErrorResponse;
import dev.abduladoni.parkingsystem.service.ParkingService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value="parking/v1/")
public class ParkingController {

    private final ParkingService parkingService;

    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }


    @Operation(summary = "Register a vehicle", description = "This API is used to register a vehicle.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Parking session has begun",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PostMapping(value = "vehicles/register")
    public ResponseEntity<ResponseDTO> registerVehicle(
            @Parameter(description = "Vehicle details", required = true)
            @Valid @RequestBody ParkingVehicleRequestDTO vehicle) {
        parkingService.registerVehicle(vehicle);
        ResponseDTO response = ResponseDTO.builder().message("Vehicle parked successfully").build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Unregister a vehicle", description = "This API is used to unregister a vehicle.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Parking session has ended",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PutMapping(value = "vehicles/{vehicleNumber}/unregister")
    public ResponseEntity<ResponseDTO> unRegisterVehicle(
            @Parameter(description = "Vehicle number", required = true)
            @PathVariable(value = "vehicleNumber") String vehicleNumber) {
        ParkingSessionDTO parkingSessionDTO = parkingService.unRegisterVehicle(vehicleNumber);
        ResponseDTO response = ResponseDTO
                .builder()
                .message(String.format("Parking session has ended, and you owe %s euros.",
                        parkingSessionDTO.getParkingFee())).build();
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Penalize unregistered vehicles", description = "This API is used to apply" +
                                                                         " penalties to unregistered vehicles.")
    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description="Success. Vehicles parked successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class)) }),
            @ApiResponse(responseCode = "400", description="Bad Request. Invalid input provided",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description="Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)) })
    })
    @PostMapping(value = "vehicles/penalties")
    public ResponseEntity<ResponseDTO> penalizeUnregisteredVehicles(
            @Parameter(description = "List of vehicle details", required = true)
            @Valid @RequestBody List<MonitorParkingVehicleRequestDTO> vehicles) {
        parkingService.penalizeUnregisteredVehicles(vehicles);
        ResponseDTO response = ResponseDTO.builder()
                                .message("Unregistered Vehicles are penalized successfully").build();
        return ResponseEntity.ok(response);
    }

}
