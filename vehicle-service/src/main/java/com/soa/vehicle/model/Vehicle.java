package com.soa.vehicle.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.soa.vehicle.model.enums.FuelType;
import com.soa.vehicle.model.enums.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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