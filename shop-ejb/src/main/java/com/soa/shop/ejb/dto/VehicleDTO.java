package com.soa.shop.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.soa.shop.model.enums.FuelType;
import com.soa.shop.model.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO implements Serializable{
    
    private Long id;
    
    private String name;
    
    private CoordinatesDTO coordinates;
    
    private LocalDateTime creationDate;
    
    private Double enginePower;
    
    private VehicleType type;
    
    private FuelType fuelType;
}