package com.soa.shop.ejb;

import com.soa.shop.ejb.dto.VehicleWheelDTO;
import jakarta.ejb.Remote;

@Remote
public interface ShopServiceRemote {
    
    VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels);
    
    VehicleWheelDTO getWheelsCount(Long vehicleId);
}