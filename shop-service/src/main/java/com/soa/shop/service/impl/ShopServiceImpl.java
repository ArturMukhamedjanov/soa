package com.soa.shop.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soa.shop.dto.VehicleDTO;
import com.soa.shop.dto.VehicleWheelDTO;
import com.soa.shop.model.VehicleWheel;
import com.soa.shop.repository.VehicleWheelRepository;
import com.soa.shop.service.ShopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShopServiceImpl implements ShopService {

    private final VehicleWheelRepository vehicleWheelRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        log.info("Adding {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);
        
        // Get existing wheels or create a new entry
        VehicleWheel vehicleWheel = vehicleWheelRepository.findByVehicleId(vehicleId)
                .orElse(new VehicleWheel(vehicleId, 0));
        
        // Add the wheels
        vehicleWheel.setWheelsCount(vehicleWheel.getWheelsCount() + numberOfWheels);
        
        // Save and return
        VehicleWheel saved = vehicleWheelRepository.save(vehicleWheel);
        return modelMapper.map(saved, VehicleWheelDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleWheelDTO getWheelsCount(Long vehicleId) {
        log.info("Getting wheel count for vehicle with ID: {}", vehicleId);
        
        // Get the wheels info or return zero if not found
        VehicleWheel vehicleWheel = vehicleWheelRepository.findByVehicleId(vehicleId)
                .orElse(new VehicleWheel(vehicleId, 0));
        
        return modelMapper.map(vehicleWheel, VehicleWheelDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> searchByEnginePowerRange(Double from, Double to) {
        // This method is now a placeholder since Shop service doesn't have direct 
        // access to vehicle data. This functionality is handled by the Vehicle Service.
        log.warn("searchByEnginePowerRange called but Shop Service doesn't have direct access to vehicle data");
        throw new UnsupportedOperationException("This operation is not supported by the Shop Service. " +
                "It should be called directly on the Vehicle Service.");
    }
}