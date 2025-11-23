package com.soa.shop.ejb.exception;

public class VehicleNotFoundException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public VehicleNotFoundException(Long vehicleId) {
        super("Vehicle with ID " + vehicleId + " not found");
    }
}