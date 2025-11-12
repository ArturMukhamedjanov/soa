package com.soa.vehicle.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.model.enums.VehicleType;

import lombok.Data;
@Entity
@Table(name = "vehicles")
@Data
public class Vehicle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @Embedded
    @NotNull
    @Valid
    private Coordinates coordinates;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    
    @NotNull
    @Positive
    @Column(nullable = false)
    private Double enginePower;
    
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;
    
    @PrePersist
    public void prePersist() {
        creationDate = LocalDateTime.now();
    }
}