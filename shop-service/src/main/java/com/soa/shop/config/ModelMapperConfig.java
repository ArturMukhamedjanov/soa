package com.soa.shop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soa.shop.dto.VehicleWheelDTO;
import com.soa.shop.model.VehicleWheel;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Configure mappings
        modelMapper.createTypeMap(VehicleWheel.class, VehicleWheelDTO.class);
        modelMapper.createTypeMap(VehicleWheelDTO.class, VehicleWheel.class);
        
        return modelMapper;
    }
}