package com.soa.shop.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soa.shop.dto.VehicleWheelDTO;
import com.soa.shop.service.ShopService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for Shop Service functionality
 * This is called by the Vehicle Service, not directly by clients
 */
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class ShopController {
    
    private final ShopService shopService;
    
    /**
     * Add wheels to a vehicle
     */
    @PostMapping("/add-wheels/{vehicle-id}/{number-of-wheels}")
    public ResponseEntity<Void> addWheelsToVehicle(
            @PathVariable("vehicle-id") @NotNull @Min(1) Long vehicleId,
            @PathVariable("number-of-wheels") @NotNull @Min(1) Integer numberOfWheels) {
        
        log.info("Adding {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);
        
        shopService.addWheelsToVehicle(vehicleId, numberOfWheels);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get wheels count for a vehicle
     */
    @GetMapping("/get-wheels-count/{vehicle-id}")
    public ResponseEntity<VehicleWheelDTO> getWheelsCount(
            @PathVariable("vehicle-id") @NotNull @Min(1) Long vehicleId) {
        
        log.info("Getting wheels count for vehicle with ID: {}", vehicleId);
        
        VehicleWheelDTO wheelsCount = shopService.getWheelsCount(vehicleId);
        return ResponseEntity.ok(wheelsCount);
    }
}