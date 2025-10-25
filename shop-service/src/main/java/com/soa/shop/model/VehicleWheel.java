package com.soa.shop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_wheels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleWheel {
    
    @Id
    @NotNull
    @Column(name = "vehicle_id")
    private Long vehicleId;
    
    @Min(0)
    @NotNull
    @Column(nullable = false)
    private Integer wheelsCount;
}