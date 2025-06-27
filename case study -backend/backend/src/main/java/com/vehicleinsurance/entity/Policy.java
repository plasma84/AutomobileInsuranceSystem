package com.vehicleinsurance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
public class Policy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String policyNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    
    @Column(nullable = false)
    @NotBlank(message = "Vehicle make is required")
    private String vehicleMake;
    
    @Column(nullable = false)
    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;
    
    @Column(nullable = false)
    @Min(value = 1900, message = "Vehicle year must be valid")
    @Max(value = 2025, message = "Vehicle year cannot be in the future")
    private Integer vehicleYear;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Vehicle registration number is required")
    private String vehicleRegistrationNumber;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Vehicle value must be positive")
    private BigDecimal vehicleValue;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CoverageType coverageType;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Premium amount must be positive")
    private BigDecimal premiumAmount;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.PROPOSAL_SUBMITTED;
    
    @Column
    private LocalDate startDate;
    
    @Column
    private LocalDate endDate;
    
    @Column
    private String additionalDetails;
    
    @Column
    private String documentsPath;
    
    @Column
    private String rejectionReason;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;
    
    @Column
    private LocalDateTime reviewedAt;
    
    // Constructors
    public Policy() {}
    
    public Policy(User user, VehicleType vehicleType, String vehicleMake, String vehicleModel,
                  Integer vehicleYear, String vehicleRegistrationNumber, BigDecimal vehicleValue,
                  CoverageType coverageType) {
        this.user = user;
        this.vehicleType = vehicleType;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.vehicleValue = vehicleValue;
        this.coverageType = coverageType;
        this.policyNumber = generatePolicyNumber();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
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
    
    public BigDecimal getPremiumAmount() { return premiumAmount; }
    public void setPremiumAmount(BigDecimal premiumAmount) { this.premiumAmount = premiumAmount; }
    
    public PolicyStatus getStatus() { return status; }
    public void setStatus(PolicyStatus status) { this.status = status; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public String getAdditionalDetails() { return additionalDetails; }
    public void setAdditionalDetails(String additionalDetails) { this.additionalDetails = additionalDetails; }
    
    public String getDocumentsPath() { return documentsPath; }
    public void setDocumentsPath(String documentsPath) { this.documentsPath = documentsPath; }
    
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(User reviewedBy) { this.reviewedBy = reviewedBy; }
    
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    private String generatePolicyNumber() {
        return "POL" + System.currentTimeMillis();
    }
    
    public boolean isExpiringSoon() {
        if (endDate == null) return false;
        return endDate.minusDays(7).isBefore(LocalDate.now()) && endDate.isAfter(LocalDate.now());
    }
}
