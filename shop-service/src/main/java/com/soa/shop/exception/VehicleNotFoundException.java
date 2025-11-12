package com.soa.shop.exception;

public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(String message) {
        super(message);
    }

    public VehicleNotFoundException(Long vehicleId) {
        super("Vehicle with ID " + vehicleId + " not found");
    }
}