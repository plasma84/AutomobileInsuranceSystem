package com.vehicleinsurance.repository;

import com.vehicleinsurance.entity.Claim;
import com.vehicleinsurance.entity.ClaimStatus;
import com.vehicleinsurance.entity.Policy;
import com.vehicleinsurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    
    List<Claim> findByPolicy(Policy policy);
    
    List<Claim> findByStatus(ClaimStatus status);
    
    List<Claim> findByStatusOrderByCreatedAtDesc(ClaimStatus status);
    
    List<Claim> findAllByOrderByCreatedAtDesc();
    
    List<Claim> findByPolicy_UserOrderByCreatedAtDesc(User user);
    
    Optional<Claim> findByClaimNumber(String claimNumber);
    
    @Query("SELECT c FROM Claim c WHERE c.policy.user.id = :userId ORDER BY c.createdAt DESC")
    List<Claim> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status = :status")
    long countByStatus(@Param("status") ClaimStatus status);
    
    @Query("SELECT c FROM Claim c WHERE c.policy.id = :policyId")
    List<Claim> findByPolicyId(@Param("policyId") Long policyId);
}
