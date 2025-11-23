package com.soa.shop.ejb;

import jakarta.ejb.Remote;

@Remote
public interface ShopServiceRemote {
    
    void addWheelsToVehicle(Long vehicleId, Integer numberOfWheels);
    
    VehicleWheelDTO getWheelsCount(Long vehicleId);
}