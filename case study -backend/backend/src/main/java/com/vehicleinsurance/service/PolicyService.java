package com.vehicleinsurance.service;

import com.vehicleinsurance.dto.PolicyProposalDto;
import com.vehicleinsurance.entity.*;
import com.vehicleinsurance.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class PolicyService {
    
    private final PolicyRepository policyRepository;
    private final PremiumCalculationService premiumCalculationService;
    
    public PolicyService(PolicyRepository policyRepository, 
                        PremiumCalculationService premiumCalculationService) {
        this.policyRepository = policyRepository;
        this.premiumCalculationService = premiumCalculationService;
    }
    
    public Policy createPolicyProposal(User user, PolicyProposalDto proposalDto) {
        // Check if vehicle registration number already exists
        if (policyRepository.existsByVehicleRegistrationNumber(proposalDto.getVehicleRegistrationNumber())) {
            throw new RuntimeException("Vehicle with this registration number is already insured");
        }
        
        Policy policy = new Policy();
        policy.setUser(user);
        policy.setVehicleType(proposalDto.getVehicleType());
        policy.setVehicleMake(proposalDto.getVehicleMake());
        policy.setVehicleModel(proposalDto.getVehicleModel());
        policy.setVehicleYear(proposalDto.getVehicleYear());
        policy.setVehicleRegistrationNumber(proposalDto.getVehicleRegistrationNumber());
        policy.setVehicleValue(proposalDto.getVehicleValue());
        policy.setCoverageType(proposalDto.getCoverageType());
        policy.setAdditionalDetails(proposalDto.getAdditionalDetails());
        policy.setPolicyNumber(generatePolicyNumber());
        
        // Calculate premium
        BigDecimal premium = premiumCalculationService.calculatePremium(
                proposalDto.getVehicleType(),
                proposalDto.getVehicleValue(),
                proposalDto.getCoverageType(),
                user.getAge(),
                proposalDto.getVehicleYear()
        );
        policy.setPremiumAmount(premium);
        
        return policyRepository.save(policy);
    }
    
    public List<Policy> getUserPolicies(User user) {
        return policyRepository.findByUser(user);
    }
    
    public List<Policy> getPoliciesByStatus(PolicyStatus status) {
        return policyRepository.findByStatus(status);
    }
    
    public Policy updatePolicyStatus(Long policyId, PolicyStatus status, User officer) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        
        policy.setStatus(status);
        policy.setReviewedBy(officer);
        policy.setReviewedAt(java.time.LocalDateTime.now());
        
        if (status == PolicyStatus.ACTIVE) {
            policy.setStartDate(LocalDate.now());
            policy.setEndDate(LocalDate.now().plusYears(1));
        }
        
        return policyRepository.save(policy);
    }
    
    public Policy findById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }
    
    public Policy findByPolicyNumber(String policyNumber) {
        return policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }
    
    public List<Policy> getPoliciesExpiringInNextWeek() {
        LocalDate now = LocalDate.now();
        LocalDate nextWeek = now.plusDays(7);
        return policyRepository.findPoliciesExpiringBetween(now, nextWeek);
    }
    
    public long getActivePoliciesCount() {
        return policyRepository.countByStatus(PolicyStatus.ACTIVE);
    }
    
    private String generatePolicyNumber() {
        return "POL" + System.currentTimeMillis();
    }
}
