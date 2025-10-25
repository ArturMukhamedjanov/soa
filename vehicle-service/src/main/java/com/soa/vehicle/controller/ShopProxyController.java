package com.soa.vehicle.controller;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soa.vehicle.client.ShopServiceClient;
import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleWheelDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This controller proxies shop-related requests to the Shop Service
 */
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ShopProxyController {
    
//    private final ShopServiceClient shopServiceClient;
//
//    /**
//     * Search vehicles by engine power range - proxies to Shop Service
//     */
//    @GetMapping("/search/by-engine-power/{from}/{to}")
//    public ResponseEntity<List<VehicleDTO>> searchByEnginePower(
//            @PathVariable @NotNull @Min(0) Double from,
//            @PathVariable @NotNull @Min(0) Double to) {
//
//        log.info("Proxying request to search vehicles with engine power between {} and {}", from, to);
//
//        if (from > to) {
//            // Swap values if from is greater than to
//            Double temp = from;
//            from = to;
//            to = temp;
//        }
//
//        List<VehicleDTO> vehicles = shopServiceClient.searchByEnginePowerRange(from, to);
//        return ResponseEntity.ok(vehicles);
//    }
//
//    /**
//     * Add wheels to a vehicle - proxies to Shop Service
//     */
//    @PostMapping("/add-wheels/{vehicle-id}/{number-of-wheels}")
//    public ResponseEntity<Void> addWheelsToVehicle(
//            @PathVariable("vehicle-id") @NotNull @Min(1) Long vehicleId,
//            @PathVariable("number-of-wheels") @NotNull @Min(1) Integer numberOfWheels) {
//
//        log.info("Proxying request to add {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);
//
//        shopServiceClient.addWheelsToVehicle(vehicleId, numberOfWheels);
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * Get wheels count for a vehicle - proxies to Shop Service
//     */
//    @GetMapping("/get-wheels-count/{vehicle-id}")
//    public ResponseEntity<VehicleWheelDTO> getWheelsCount(
//            @PathVariable("vehicle-id") @NotNull @Min(1) Long vehicleId) {
//
//        log.info("Proxying request to get wheels count for vehicle with ID: {}", vehicleId);
//
//        VehicleWheelDTO wheelsCount = shopServiceClient.getWheelsCount(vehicleId);
//        return ResponseEntity.ok(wheelsCount);
//    }
}