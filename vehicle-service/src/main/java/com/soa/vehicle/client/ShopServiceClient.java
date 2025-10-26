package com.soa.vehicle.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.soa.vehicle.dto.VehicleWheelDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShopServiceClient{

    private final RestTemplate restTemplate;

    private String shopServiceBaseUrl = "http://localhost:8081/shop";

    public void addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        try {
            String url = shopServiceBaseUrl + "/add-wheels/{vehicle-id}/{number-of-wheels}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class,
                    vehicleId,
                    numberOfWheels
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully added {} wheels to vehicle ID: {}", numberOfWheels, vehicleId);
            } else {
                log.error("Failed to add wheels to vehicle ID: {}. Status: {}", vehicleId, response.getStatusCode());
                throw new RuntimeException("Failed to add wheels to vehicle");
            }

        } catch (Exception e) {
            log.error("Error adding wheels to vehicle ID: {}", vehicleId, e);
            throw new RuntimeException("Shop service unavailable", e);
        }
    }

    public VehicleWheelDTO getWheelsCount(Long vehicleId) {
        try {
            String url = shopServiceBaseUrl + "/get-wheels-count/{vehicle-id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<VehicleWheelDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    VehicleWheelDTO.class,
                    vehicleId
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully retrieved wheels count for vehicle ID: {}", vehicleId);
                return response.getBody();
            } else {
                log.error("Failed to get wheels count for vehicle ID: {}. Status: {}", vehicleId, response.getStatusCode());
                throw new RuntimeException("Failed to get wheels count");
            }

        } catch (Exception e) {
            log.error("Error getting wheels count for vehicle ID: {}", vehicleId, e);
            throw new RuntimeException("Shop service unavailable", e);
        }
    }
}