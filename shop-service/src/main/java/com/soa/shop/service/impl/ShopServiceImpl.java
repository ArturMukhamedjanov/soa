package com.soa.shop.service.impl;

import com.soa.shop.client.VehicleServiceClient;
import com.soa.shop.exception.VehicleNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final VehicleServiceClient vehicleServiceClient;
    private final VehicleWheelRepository vehicleWheelRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        log.info("Adding {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);

        if(!vehicleServiceClient.checkVehicleExists(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
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

        if(!vehicleServiceClient.checkVehicleExists(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
        // Get the wheels info or return zero if not found
        VehicleWheel vehicleWheel = vehicleWheelRepository.findByVehicleId(vehicleId)
                .orElse(new VehicleWheel(vehicleId, 0));
        
        return modelMapper.map(vehicleWheel, VehicleWheelDTO.class);
    }
}