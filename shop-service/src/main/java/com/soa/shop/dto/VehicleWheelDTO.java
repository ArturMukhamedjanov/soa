package com.soa.shop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWheelDTO {
    
    @NotNull
    private Long vehicleId;
    
    @NotNull
    @Min(0)
    private Integer wheelsCount;
}