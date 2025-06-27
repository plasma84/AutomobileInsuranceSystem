package com.vehicleinsurance.service;

import com.vehicleinsurance.entity.Claim;
import com.vehicleinsurance.entity.ClaimStatus;
import com.vehicleinsurance.entity.Policy;
import com.vehicleinsurance.entity.User;
import com.vehicleinsurance.repository.ClaimRepository;
import com.vehicleinsurance.repository.PolicyRepository;
import com.vehicleinsurance.dto.ClaimDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimService(ClaimRepository claimRepository, PolicyRepository policyRepository) {
        this.claimRepository = claimRepository;
        this.policyRepository = policyRepository;
    }

    public Claim createClaim(ClaimDto claimDto, User user) {
        Optional<Policy> policyOpt = policyRepository.findById(claimDto.getPolicyId());
        if (policyOpt.isEmpty()) {
            throw new IllegalArgumentException("Policy not found");
        }

        Policy policy = policyOpt.get();
        if (!policy.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only create claims for your own policies");
        }

        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setClaimNumber("CLM" + System.currentTimeMillis());
        claim.setIncidentDescription(claimDto.getIncidentDescription());
        claim.setClaimAmount(claimDto.getClaimAmount());
        claim.setIncidentDate(claimDto.getIncidentDate());
        claim.setIncidentLocation(claimDto.getIncidentLocation());
        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());

        return claimRepository.save(claim);
    }

    public List<Claim> getUserClaims(User user) {
        return claimRepository.findByPolicy_UserOrderByCreatedAtDesc(user);
    }

    public List<Claim> getClaimsByStatus(ClaimStatus status) {
        return claimRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public List<Claim> getAllClaims() {
        return claimRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Claim> getClaimById(Long id) {
        return claimRepository.findById(id);
    }

    public Claim updateClaimStatus(Long claimId, ClaimStatus newStatus, User updatedBy) {
        Optional<Claim> claimOpt = claimRepository.findById(claimId);
        if (claimOpt.isEmpty()) {
            throw new IllegalArgumentException("Claim not found");
        }

        Claim claim = claimOpt.get();
        claim.setStatus(newStatus);
        claim.setUpdatedAt(LocalDateTime.now());

        if (newStatus == ClaimStatus.APPROVED || newStatus == ClaimStatus.REJECTED) {
            claim.setProcessedBy(updatedBy);
            claim.setProcessedAt(LocalDateTime.now());
        }

        return claimRepository.save(claim);
    }

    public void deleteClaim(Long id, User user) {
        Optional<Claim> claimOpt = claimRepository.findById(id);
        if (claimOpt.isEmpty()) {
            throw new IllegalArgumentException("Claim not found");
        }

        Claim claim = claimOpt.get();
        if (!claim.getPolicy().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only delete your own claims");
        }

        if (claim.getStatus() != ClaimStatus.SUBMITTED) {
            throw new IllegalArgumentException("Only submitted claims can be deleted");
        }

        claimRepository.delete(claim);
    }

    public long getClaimsCountByStatus(ClaimStatus status) {
        return claimRepository.countByStatus(status);
    }

    public long getTotalClaimsCount() {
        return claimRepository.count();
    }
}
