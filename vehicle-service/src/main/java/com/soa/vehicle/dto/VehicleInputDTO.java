package com.soa.vehicle.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.model.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleInputDTO {
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotNull(message = "Coordinates cannot be null")
    @Valid
    private CoordinatesDTO coordinates;
    
    @NotNull(message = "Engine power cannot be null")
    @Positive(message = "Engine power must be positive")
    private Double enginePower;
    
    private VehicleType type;
    
    @NotNull(message = "Fuel type cannot be null")
    private FuelType fuelType;
}