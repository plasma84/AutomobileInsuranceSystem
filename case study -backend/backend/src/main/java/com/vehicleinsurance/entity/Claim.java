package com.vehicleinsurance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String claimNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;
    
    @Column(nullable = false)
    @NotBlank(message = "Incident description is required")
    private String incidentDescription;
    
    @Column(nullable = false)
    @Past(message = "Incident date must be in the past")
    private LocalDate incidentDate;
    
    @Column(nullable = false)
    @NotBlank(message = "Incident location is required")
    private String incidentLocation;
    
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Claim amount must be positive")
    private BigDecimal claimAmount;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.SUBMITTED;
    
    @Column
    private String documentsPath;
    
    @Column
    private String officerComments;
    
    @Column
    private BigDecimal approvedAmount;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by")
    private User processedBy;
    
    @Column
    private LocalDateTime processedAt;
    
    // Constructors
    public Claim() {}
    
    public Claim(Policy policy, String incidentDescription, LocalDate incidentDate,
                 String incidentLocation, BigDecimal claimAmount) {
        this.policy = policy;
        this.incidentDescription = incidentDescription;
        this.incidentDate = incidentDate;
        this.incidentLocation = incidentLocation;
        this.claimAmount = claimAmount;
        this.claimNumber = generateClaimNumber();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getClaimNumber() { return claimNumber; }
    public void setClaimNumber(String claimNumber) { this.claimNumber = claimNumber; }
    
    public Policy getPolicy() { return policy; }
    public void setPolicy(Policy policy) { this.policy = policy; }
    
    public String getIncidentDescription() { return incidentDescription; }
    public void setIncidentDescription(String incidentDescription) { this.incidentDescription = incidentDescription; }
    
    public LocalDate getIncidentDate() { return incidentDate; }
    public void setIncidentDate(LocalDate incidentDate) { this.incidentDate = incidentDate; }
    
    public String getIncidentLocation() { return incidentLocation; }
    public void setIncidentLocation(String incidentLocation) { this.incidentLocation = incidentLocation; }
    
    public BigDecimal getClaimAmount() { return claimAmount; }
    public void setClaimAmount(BigDecimal claimAmount) { this.claimAmount = claimAmount; }
    
    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }
    
    public String getDocumentsPath() { return documentsPath; }
    public void setDocumentsPath(String documentsPath) { this.documentsPath = documentsPath; }
    
    public String getOfficerComments() { return officerComments; }
    public void setOfficerComments(String officerComments) { this.officerComments = officerComments; }
    
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public User getProcessedBy() { return processedBy; }
    public void setProcessedBy(User processedBy) { this.processedBy = processedBy; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    private String generateClaimNumber() {
        return "CLM" + System.currentTimeMillis();
    }
}
