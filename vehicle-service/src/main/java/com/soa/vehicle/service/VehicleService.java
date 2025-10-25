package com.soa.vehicle.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleInputDTO;
import com.soa.vehicle.model.enums.FuelType;

public interface VehicleService {
    
    /**
     * Get all vehicles with pagination and sorting
     */
    Page<VehicleDTO> getAllVehicles(Pageable pageable);
    
    /**
     * Create a new vehicle
     */
    VehicleDTO createVehicle(VehicleInputDTO vehicleInput);
    
    /**
     * Get vehicle by ID
     */
    VehicleDTO getVehicleById(Long id);
    
    /**
     * Update existing vehicle
     */
    VehicleDTO updateVehicle(Long id, VehicleInputDTO vehicleInput);
    
    /**
     * Delete vehicle by ID
     */
    void deleteVehicle(Long id);
    
    /**
     * Calculate sum of all engines power
     */
    Double sumEnginesPower();
    
    /**
     * Filter vehicles by name prefix
     */
    List<VehicleDTO> filterByNamePrefix(String prefix);
    
    /**
     * Filter vehicles by fuel type
     */
    List<VehicleDTO> filterByFuelType(FuelType fuelType);
    
    /**
     * Search vehicles by engine power range
     */
    List<VehicleDTO> searchByEnginePowerRange(Double from, Double to);
}