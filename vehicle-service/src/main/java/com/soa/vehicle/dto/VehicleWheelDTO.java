package com.soa.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWheelDTO {
    
    private Long vehicleId;
    
    private Integer wheelsCount;
}