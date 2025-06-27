package com.vehicleinsurance.dto;

import com.vehicleinsurance.entity.VehicleType;
import com.vehicleinsurance.entity.CoverageType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PolicyProposalDto {
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
    
    @NotBlank(message = "Vehicle make is required")
    private String vehicleMake;
    
    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;
    
    @NotNull(message = "Vehicle year is required")
    @Min(value = 1900, message = "Vehicle year must be valid")
    @Max(value = 2025, message = "Vehicle year cannot be in the future")
    private Integer vehicleYear;
    
    @NotBlank(message = "Vehicle registration number is required")
    private String vehicleRegistrationNumber;
    
    @NotNull(message = "Vehicle value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Vehicle value must be positive")
    private BigDecimal vehicleValue;
    
    @NotNull(message = "Coverage type is required")
    private CoverageType coverageType;
    
    private String additionalDetails;
    
    // Constructors
    public PolicyProposalDto() {}
    
    // Getters and Setters
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
    
    public String getVehicleMake() { return vehicleMake; }
    public void setVehicleMake(String vehicleMake) { this.vehicleMake = vehicleMake; }
    
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    
    public Integer getVehicleYear() { return vehicleYear; }
    public void setVehicleYear(Integer vehicleYear) { this.vehicleYear = vehicleYear; }
    
    public String getVehicleRegistrationNumber() { return vehicleRegistrationNumber; }
    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) { 
        this.vehicleRegistrationNumber = vehicleRegistrationNumber; 
    }
    
    public BigDecimal getVehicleValue() { return vehicleValue; }
    public void setVehicleValue(BigDecimal vehicleValue) { this.vehicleValue = vehicleValue; }
    
    public CoverageType getCoverageType() { return coverageType; }
    public void setCoverageType(CoverageType coverageType) { this.coverageType = coverageType; }
    
    public String getAdditionalDetails() { return additionalDetails; }
    public void setAdditionalDetails(String additionalDetails) { this.additionalDetails = additionalDetails; }
}
