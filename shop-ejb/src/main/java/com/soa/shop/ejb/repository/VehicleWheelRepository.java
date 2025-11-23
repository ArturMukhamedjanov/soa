package com.soa.shop.ejb.repository;

import com.soa.shop.ejb.model.VehicleWheel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import jakarta.ejb.Stateless;

@Stateless
public class VehicleWheelRepository {
    
    @PersistenceContext(unitName = "shopPU")
    private EntityManager entityManager;
    
    /**
     * Find vehicle wheels by vehicle ID
     */
    public Optional<VehicleWheel> findByVehicleId(Long vehicleId) {
        try {
            VehicleWheel result = entityManager.createQuery(
                    "SELECT v FROM VehicleWheel v WHERE v.vehicleId = :vehicleId", 
                    VehicleWheel.class)
                    .setParameter("vehicleId", vehicleId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Save a VehicleWheel entity
     */
    public VehicleWheel save(VehicleWheel vehicleWheel) {
        if (entityManager.find(VehicleWheel.class, vehicleWheel.getVehicleId()) != null) {
            return entityManager.merge(vehicleWheel);
        } else {
            entityManager.persist(vehicleWheel);
            return vehicleWheel;
        }
    }
}