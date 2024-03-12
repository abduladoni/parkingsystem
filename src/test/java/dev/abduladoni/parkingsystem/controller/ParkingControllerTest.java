package dev.abduladoni.parkingsystem.controller;

import dev.abduladoni.parkingsystem.dto.ParkingSessionDTO;
import dev.abduladoni.parkingsystem.dto.ParkingVehicleRequestDTO;
import dev.abduladoni.parkingsystem.exception.InvalidParkingRequestException;
import dev.abduladoni.parkingsystem.service.ParkingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingController.class)
public class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingService parkingService;

    @AfterEach
    public void afterEach(){
        reset(parkingService);
    }

    @Test
    @DisplayName("Should park a vehicle successfully")
    public void shouldParkVehicleSuccessfully() throws Exception {
        doNothing().when(parkingService).registerVehicle(any(ParkingVehicleRequestDTO.class));

        mockMvc.perform(post("/parking/v1/vehicles/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vehicleNumber\":\"AA-690-A\",\"streetName\":\"Azure\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request when invalid vehicle details are provided for parking")
    public void shouldReturnBadRequestWhenInvalidVehicleDetailsAreProvidedForParking() throws Exception {
        doThrow(new InvalidParkingRequestException("Invalid vehicle details"))
                .when(parkingService).registerVehicle(any(ParkingVehicleRequestDTO.class));
        mockMvc.perform(post("/parking/v1/vehicles/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"vehicleNumber\":\"\",\"streetName\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should unpark a vehicle successfully")
    public void shouldUnparkVehicleSuccessfully() throws Exception {
        when(parkingService.unRegisterVehicle(anyString()))
                .thenReturn(ParkingSessionDTO.builder().parkingFee("10.0").build());

        mockMvc.perform(put("/parking/v1/vehicles/AA-690-A/unregister"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request when invalid vehicle number is provided for unparking")
    public void shouldReturnBadRequestWhenInvalidVehicleNumberIsProvidedForUnparking() throws Exception {
         doThrow(new InvalidParkingRequestException("No active parking session found for vehicle: AA-691-A"))
                .when(parkingService).unRegisterVehicle("AA-691-A");
        mockMvc.perform(put("/parking/v1/vehicles/AA-691-A/unregister"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should penalize unregistered vehicles successfully")
    public void shouldPenalizeUnregisteredVehiclesSuccessfully() throws Exception {

        doNothing().when(parkingService).penalizeUnregisteredVehicles(any());

        mockMvc.perform(post("/parking/v1/vehicles/penalties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"vehicleNumber\":\"AA-691-A\",\"streetName\":\"Jakarta\"}]"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bad request when invalid vehicle details are provided for penalties")
    public void shouldReturnBadRequestWhenInvalidVehicleDetailsAreProvidedForPenalties() throws Exception {
        doThrow(new InvalidParkingRequestException("Invalid Street Name: Main Land"))
                .when(parkingService).penalizeUnregisteredVehicles(any());
        mockMvc.perform(post("/parking/v1/vehicles/penalties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"vehicleNumber\":\"AA-990-B\",\"streetName\":\"Main Land\"}]"))
                .andExpect(status().isBadRequest());
    }
}