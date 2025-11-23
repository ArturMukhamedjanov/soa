package com.soa.shop.ejb.client;

import lombok.extern.slf4j.Slf4j;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.ejb.Stateless;

import java.io.IOException;

@Slf4j
@Stateless
public class VehicleServiceClient {
    
    private String vehicleServiceBaseUrl = "https://localhost:8445/vihicle-service/vehicles";

    /**
     * Checks if a vehicle exists by ID
     * @param vehicleId ID of the vehicle
     * @return true if the vehicle exists, false otherwise
     */
    public boolean checkVehicleExists(Long vehicleId) {
        try {
            URL url = new URL(vehicleServiceBaseUrl + "/" + vehicleId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            int status = connection.getResponseCode();
            if (status == 200) {
                log.info("Vehicle with ID: {} exists", vehicleId);
                return true;
            } else {
                log.warn("Vehicle with ID: {} not found. Status: {}", vehicleId, status);
                return false;
            }
        } catch (IOException e) {
            log.error("Error checking if vehicle exists: {}", e.getMessage());
            return false;
        }
    }
}