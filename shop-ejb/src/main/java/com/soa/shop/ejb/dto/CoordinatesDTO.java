package com.soa.shop.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDTO implements Serializable{
    
    private Integer x;
    
    @NotNull(message = "Y coordinate cannot be null")
    private Integer y;
}