package com.soa.vehicle.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleInputDTO;
import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.service.VehicleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Validated
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "20") @Min(1) Integer size,
            @RequestParam(required = false) String sort) {

        // Adjust page to be 0-based for Spring Data
        int pageIndex = page - 1;

        PageRequest pageRequest;
        if (sort != null && !sort.isEmpty()) {
            // Parse sort parameter and create PageRequest with sorting
            pageRequest = PageRequest.of(pageIndex, size, Sort.by(sort));
        } else {
            pageRequest = PageRequest.of(pageIndex, size);
        }

        Page<VehicleDTO> vehiclesPage = vehicleService.getAllVehicles(pageRequest);
        List<VehicleDTO> vehicles = vehiclesPage.getContent();

        return ResponseEntity.ok(vehicles);
    }

    @PostMapping
    public ResponseEntity<Void> createVehicle(@Valid @RequestBody VehicleInputDTO vehicleInput) {
        VehicleDTO created = vehicleService.createVehicle(vehicleInput);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable @Min(1) Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody VehicleInputDTO vehicleInput) {

        VehicleDTO updated = vehicleService.updateVehicle(id, vehicleInput);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(@PathVariable @Min(1) Long id) {
        vehicleService.deleteVehicle(id);
    }

    @GetMapping("/sum-engine-power")
    public ResponseEntity<Map<String, Double>> getSumEnginePower() {
        Double sum = vehicleService.sumEnginesPower();
        return ResponseEntity.ok(Map.of("sum", sum != null ? sum : 0.0));
    }

    @GetMapping("/filter/name/{prefix}")
    public ResponseEntity<List<VehicleDTO>> filterByNamePrefix(@PathVariable @NotBlank String prefix) {
        List<VehicleDTO> vehicles = vehicleService.filterByNamePrefix(prefix);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/filter/fuel-type/{fuelType}")
    public ResponseEntity<List<VehicleDTO>> filterByFuelType(@PathVariable FuelType fuelType) {
        List<VehicleDTO> vehicles = vehicleService.filterByFuelType(fuelType);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/shop/search/by-engine-power/{from}/{to}")
    public ResponseEntity<List<VehicleDTO>> searchByEnginePowerRange(
            @PathVariable @Min(0) Double from,
            @PathVariable @Min(0) Double to) {

        List<VehicleDTO> vehicles = vehicleService.searchByEnginePowerRange(from, to);
        return ResponseEntity.ok(vehicles);
    }
}