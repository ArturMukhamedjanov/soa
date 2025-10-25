package com.soa.vehicle.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {

    public ErrorResponseDTO(String message) {
        this.message = message;
    }
    
    private String message;
}