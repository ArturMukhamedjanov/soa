package com.soa.vehicle.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soa.vehicle.dto.CoordinatesDTO;
import com.soa.vehicle.dto.VehicleDTO;
import com.soa.vehicle.dto.VehicleInputDTO;
import com.soa.vehicle.model.Coordinates;
import com.soa.vehicle.model.Vehicle;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure mappings
        modelMapper.createTypeMap(Vehicle.class, VehicleDTO.class);
        modelMapper.createTypeMap(VehicleInputDTO.class, Vehicle.class)
                .addMappings(mapper -> mapper.skip(Vehicle::setId))
                .addMappings(mapper -> mapper.skip(Vehicle::setCreationDate));
        modelMapper.createTypeMap(Coordinates.class, CoordinatesDTO.class);
        modelMapper.createTypeMap(CoordinatesDTO.class, Coordinates.class);
        
        return modelMapper;
    }
}