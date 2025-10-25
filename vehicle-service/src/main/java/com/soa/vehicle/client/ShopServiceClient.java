package com.soa.vehicle.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleWheelDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ShopServiceClient {
    /**
     * Add wheels to a vehicle
     */
    public void addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        
    }

    /**
     * Get wheels count for a vehicle
     */
    public VehicleWheelDTO getWheelsCount(Long vehicleId) {
        return null;
    }

    /**
     * Search vehicles by engine power range
     */
    public List<VehicleDTO> searchByEnginePowerRange(Double from, Double to) {
        return List.of();
    }
}