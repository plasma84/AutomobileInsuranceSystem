package com.vehicleinsurance.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimDto {
    
    @NotNull(message = "Policy ID is required")
    private Long policyId;
    
    @NotBlank(message = "Incident description is required")
    @Size(min = 10, max = 1000, message = "Incident description must be between 10 and 1000 characters")
    private String incidentDescription;
    
    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "100.0", message = "Claim amount must be at least ₹100")
    @DecimalMax(value = "10000000.0", message = "Claim amount cannot exceed ₹1,00,00,000")
    private BigDecimal claimAmount;
    
    @NotNull(message = "Incident date is required")
    @PastOrPresent(message = "Incident date cannot be in the future")
    private LocalDate incidentDate;
    
    @NotBlank(message = "Incident location is required")
    @Size(max = 200, message = "Incident location cannot exceed 200 characters")
    private String incidentLocation;
    
    // Constructors
    public ClaimDto() {}
    
    public ClaimDto(Long policyId, String incidentDescription, BigDecimal claimAmount, 
                   LocalDate incidentDate, String incidentLocation) {
        this.policyId = policyId;
        this.incidentDescription = incidentDescription;
        this.claimAmount = claimAmount;
        this.incidentDate = incidentDate;
        this.incidentLocation = incidentLocation;
    }
    
    // Getters and Setters
    public Long getPolicyId() {
        return policyId;
    }
    
    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }
    
    public String getIncidentDescription() {
        return incidentDescription;
    }
    
    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }
    
    public BigDecimal getClaimAmount() {
        return claimAmount;
    }
    
    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }
    
    public LocalDate getIncidentDate() {
        return incidentDate;
    }
    
    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }
    
    public String getIncidentLocation() {
        return incidentLocation;
    }
    
    public void setIncidentLocation(String incidentLocation) {
        this.incidentLocation = incidentLocation;
    }
}
