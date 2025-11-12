package com.soa.vehicle.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.soa.vehicle.model.Vehicle;
import com.soa.vehicle.model.enums.FuelType;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Find vehicles by name starting with given prefix
    List<Vehicle> findByNameStartingWith(String prefix);
    
    // Find vehicles by fuel type
    List<Vehicle> findByFuelType(FuelType fuelType);
    
    // Calculate sum of engine power of all vehicles
    @Query("SELECT SUM(v.enginePower) FROM Vehicle v")
    Double sumEnginesPower();
    
    // Find vehicles with engine power between range
    @Query("SELECT v FROM Vehicle v WHERE v.enginePower BETWEEN :from AND :to")
    List<Vehicle> findByEnginePowerBetween(@Param("from") Double from, @Param("to") Double to);
}