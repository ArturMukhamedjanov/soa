package com.soa.shop.service;

import com.soa.shop.dto.VehicleDTO;
import com.soa.shop.dto.VehicleWheelDTO;

import java.util.List;

public interface ShopService {
    
    /**
     * Add wheels to a vehicle
     * 
     * @param vehicleId the ID of the vehicle
     * @param numberOfWheels the number of wheels to add
     * @return the updated VehicleWheelDTO
     */
    VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels);
    
    /**
     * Get the wheel count for a vehicle
     * 
     * @param vehicleId the ID of the vehicle
     * @return the VehicleWheelDTO
     */
    VehicleWheelDTO getWheelsCount(Long vehicleId);

}