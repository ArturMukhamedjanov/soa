package com.soa.vehicle.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleInputDTO;
import com.soa.vehicle.model.Vehicle;
import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.repository.VehicleRepository;
import com.soa.vehicle.service.VehicleService;
import com.soa.vehicle.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
        return vehicleRepository.findAll(pageable)
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class));
    }

    @Override
    public VehicleDTO createVehicle(VehicleInputDTO vehicleInput) {
        Vehicle vehicle = modelMapper.map(vehicleInput, Vehicle.class);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return modelMapper.map(savedVehicle, VehicleDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public VehicleDTO updateVehicle(Long id, VehicleInputDTO vehicleInput) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));
        
        // Update fields except ID and creation date
        modelMapper.map(vehicleInput, existingVehicle);
        
        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return modelMapper.map(updatedVehicle, VehicleDTO.class);
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id " + id);
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Double sumEnginesPower() {
        return vehicleRepository.sumEnginesPower();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> filterByNamePrefix(String prefix) {
        return vehicleRepository.findByNameStartingWith(prefix)
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> filterByFuelType(FuelType fuelType) {
        return vehicleRepository.findByFuelType(fuelType)
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> searchByEnginePowerRange(Double from, Double to) {
        return vehicleRepository.findByEnginePowerBetween(from, to)
                .stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }
}