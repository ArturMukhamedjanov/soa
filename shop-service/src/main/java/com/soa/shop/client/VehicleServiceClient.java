package com.soa.shop.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceClient {

    private final RestTemplate restTemplate;

    private String vehicleServiceBaseUrl = "https://localhost:25401/vihicle-service/vehicles";

    /**
     * Проверяет существование Vehicle по ID
     * @param vehicleId ID транспортного средства
     * @return true если Vehicle существует, false если нет
     */
    public boolean checkVehicleExists(Long vehicleId) {
        try {
            String url = vehicleServiceBaseUrl + "/{id}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    Object.class,
                    vehicleId
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Vehicle with ID: {} exists", vehicleId);
                return true;
            } else {
                log.warn("Vehicle with ID: {} not found. Status: {}", vehicleId, response.getStatusCode());
                return false;
            }

        } catch (HttpClientErrorException.NotFound e) {
            // Обработка 404 - транспортное средство не найдено
            log.warn("Vehicle with ID: {} not found. Response: {}", vehicleId, e.getResponseBodyAsString());
            return false;
        } catch (HttpClientErrorException e) {
            // Обработка других HTTP ошибок (4xx)
            log.warn("HTTP error when checking vehicle with ID: {}. Status: {}, Response: {}",
                    vehicleId, e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            // Обработка всех остальных ошибок (сетевые, SSL и т.д.)
            log.warn("Error checking vehicle existence for ID: {}. Error: {}", vehicleId, e.getMessage());
            return false;
        }
    }
}