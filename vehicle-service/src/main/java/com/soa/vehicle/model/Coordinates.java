package com.soa.vehicle.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates {
    
    private Integer x;
    
    @NotNull
    private Integer y;
}