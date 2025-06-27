package com.vehicleinsurance.repository;

import com.vehicleinsurance.entity.Policy;
import com.vehicleinsurance.entity.PolicyStatus;
import com.vehicleinsurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    
    List<Policy> findByUser(User user);
    
    List<Policy> findByStatus(PolicyStatus status);
    
    Optional<Policy> findByPolicyNumber(String policyNumber);
    
    boolean existsByVehicleRegistrationNumber(String vehicleRegistrationNumber);
    
    @Query("SELECT p FROM Policy p WHERE p.endDate BETWEEN :startDate AND :endDate")
    List<Policy> findPoliciesExpiringBetween(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(p) FROM Policy p WHERE p.status = :status")
    long countByStatus(@Param("status") PolicyStatus status);
    
    @Query("SELECT p FROM Policy p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Policy> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
