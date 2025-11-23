package com.soa.shop.ejb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
    @Column(nullable = false, name="wheels_count")
    private Integer wheelsCount;
}