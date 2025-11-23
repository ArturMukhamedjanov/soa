package com.soa.shop.ejb.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWheelDTO implements Serializable{
    private Long vehicleId;
    private Integer wheelsCount;
}