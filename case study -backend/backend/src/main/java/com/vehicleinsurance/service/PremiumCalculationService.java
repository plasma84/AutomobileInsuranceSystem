package com.vehicleinsurance.service;

import com.vehicleinsurance.entity.CoverageType;
import com.vehicleinsurance.entity.VehicleType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class PremiumCalculationService {
    
    private static final BigDecimal BASE_RATE = new BigDecimal("0.05"); // 5% base rate
    private static final BigDecimal YOUNG_DRIVER_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal OLD_VEHICLE_MULTIPLIER = new BigDecimal("1.3");
    
    public BigDecimal calculatePremium(VehicleType vehicleType, BigDecimal vehicleValue, 
                                     CoverageType coverageType, int driverAge, int vehicleYear) {
        
        BigDecimal basePremium = vehicleValue.multiply(BASE_RATE);
        
        // Vehicle type multiplier
        BigDecimal vehicleTypeMultiplier = getVehicleTypeMultiplier(vehicleType);
        basePremium = basePremium.multiply(vehicleTypeMultiplier);
        
        // Coverage type multiplier
        BigDecimal coverageMultiplier = getCoverageTypeMultiplier(coverageType);
        basePremium = basePremium.multiply(coverageMultiplier);
        
        // Age factor
        if (driverAge < 25) {
            basePremium = basePremium.multiply(YOUNG_DRIVER_MULTIPLIER);
        }
        
        // Vehicle age factor
        int vehicleAge = LocalDate.now().getYear() - vehicleYear;
        if (vehicleAge > 10) {
            basePremium = basePremium.multiply(OLD_VEHICLE_MULTIPLIER);
        }
        
        return basePremium.setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal getVehicleTypeMultiplier(VehicleType vehicleType) {
        return switch (vehicleType) {
            case CAR -> new BigDecimal("1.0");
            case MOTORCYCLE -> new BigDecimal("0.8");
            case TRUCK -> new BigDecimal("1.5");
            case CAMPER_VAN -> new BigDecimal("1.2");
        };
    }
    
    private BigDecimal getCoverageTypeMultiplier(CoverageType coverageType) {
        return switch (coverageType) {
            case BASIC -> new BigDecimal("1.0");
            case COMPREHENSIVE -> new BigDecimal("1.5");
            case PREMIUM -> new BigDecimal("2.0");
        };
    }
}
