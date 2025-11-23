package com.soa.shop.service.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.stereotype.Service;

import com.soa.shop.dto.VehicleWheelDTO;
import com.soa.shop.service.ShopService;
import com.soa.shop.ejb.ShopServiceRemote;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopServiceImpl implements ShopService {
    
    /**
     * Gets the remote EJB using JNDI lookup
     */
    private ShopServiceRemote getShopServiceRemote() {
        try {
            Context context = new InitialContext();
            return (ShopServiceRemote) context.lookup("java:global/shop-ejb-0.0.1-SNAPSHOT/ShopServiceBean!com.soa.shop.ejb.ShopServiceRemote");
        } catch (NamingException e) {
            log.error("Failed to lookup ShopServiceRemote: {}", e.getMessage());
            throw new RuntimeException("Failed to lookup EJB", e);
        }
    }

    @Override
    public VehicleWheelDTO addWheelsToVehicle(Long vehicleId, Integer numberOfWheels) {
        log.info("Web: Adding {} wheels to vehicle with ID: {}", numberOfWheels, vehicleId);
        
        // Delegate to the EJB
        ShopServiceRemote shopService = getShopServiceRemote();
        com.soa.shop.ejb.dto.VehicleWheelDTO ejbResult = shopService.addWheelsToVehicle(vehicleId, numberOfWheels);
        
        // Convert from EJB DTO to web service DTO
        return mapFromEjbDto(ejbResult);
    }

    @Override
    public VehicleWheelDTO getWheelsCount(Long vehicleId) {
        log.info("Web: Getting wheel count for vehicle with ID: {}", vehicleId);
        
        // Delegate to the EJB
        ShopServiceRemote shopService = getShopServiceRemote();
        com.soa.shop.ejb.dto.VehicleWheelDTO ejbResult = shopService.getWheelsCount(vehicleId);
        
        // Convert from EJB DTO to web service DTO
        return mapFromEjbDto(ejbResult);
    }
    
    /**
     * Maps an EJB DTO to a web service DTO
     */
    private VehicleWheelDTO mapFromEjbDto(com.soa.shop.ejb.dto.VehicleWheelDTO ejbDto) {
        VehicleWheelDTO dto = new VehicleWheelDTO();
        dto.setVehicleId(ejbDto.getVehicleId());
        dto.setWheelsCount(ejbDto.getWheelsCount());
        return dto;
    }
}