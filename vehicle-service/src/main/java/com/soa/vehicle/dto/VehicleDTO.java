package com.soa.vehicle.dto;

import java.time.LocalDateTime;

import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.model.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    
    private Long id;
    
    private String name;
    
    private CoordinatesDTO coordinates;
    
    private LocalDateTime creationDate;
    
    private Double enginePower;
    
    private VehicleType type;
    
    private FuelType fuelType;
}