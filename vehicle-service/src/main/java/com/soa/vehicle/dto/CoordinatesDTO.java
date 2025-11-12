package com.soa.vehicle.dto;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDTO {
    
    private Integer x;
    
    @NotNull(message = "Y coordinate cannot be null")
    private Integer y;
}