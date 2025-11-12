package com.soa.shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soa.shop.model.VehicleWheel;

@Repository
public interface VehicleWheelRepository extends JpaRepository<VehicleWheel, Long> {
    
    /**
     * Find vehicle wheels by vehicle ID
     */
    Optional<VehicleWheel> findByVehicleId(Long vehicleId);
}