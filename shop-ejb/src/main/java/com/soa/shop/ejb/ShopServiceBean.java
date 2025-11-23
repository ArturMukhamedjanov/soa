package com.soa.shop.ejb;

import com.soa.shop.ejb.client.VehicleServiceClient;
import com.soa.shop.ejb.dto.VehicleWheelDTO;
import com.soa.shop.ejb.exception.VehicleNotFoundException;
import com.soa.shop.ejb.model.VehicleWheel;
import com.soa.shop.ejb.repository.VehicleWheelRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Remote(ShopServiceRemote.class)
@Slf4j
public class ShopServiceBean implements ShopServiceRemote {

    @EJB
    private VehicleWheelRepository vehicleWheelRepository;
    
    @EJB
    private VehicleServiceClient vehicleServiceClient;
    
    @Override
    public VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        log.info("EJB: Adding {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);

        if (!vehicleServiceClient.checkVehicleExists(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
        // Get existing wheels or create a new entry
        VehicleWheel vehicleWheel = vehicleWheelRepository.findByVehicleId(vehicleId)
                .orElse(new VehicleWheel(vehicleId, 0));
        
        // Add the wheels
        vehicleWheel.setWheelsCount(vehicleWheel.getWheelsCount() + numberOfWheels);
        
        // Save and return
        VehicleWheel saved = vehicleWheelRepository.save(vehicleWheel);
        return mapToDTO(saved);
    }

    @Override
    public VehicleWheelDTO getWheelsCount(Long vehicleId) {
        log.info("EJB: Getting wheel count for vehicle with ID: {}", vehicleId);

        if (!vehicleServiceClient.checkVehicleExists(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
        // Get the wheels info or return zero if not found
        VehicleWheel vehicleWheel = vehicleWheelRepository.findByVehicleId(vehicleId)
                .orElse(new VehicleWheel(vehicleId, 0));
        
        return mapToDTO(vehicleWheel);
    }
    
    /**
     * Maps a VehicleWheel entity to a VehicleWheelDTO
     */
    private VehicleWheelDTO mapToDTO(VehicleWheel vehicleWheel) {
        return new VehicleWheelDTO(
                vehicleWheel.getVehicleId(),
                vehicleWheel.getWheelsCount()
        );
    }
}