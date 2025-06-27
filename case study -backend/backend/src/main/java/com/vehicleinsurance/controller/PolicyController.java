package com.vehicleinsurance.controller;

import com.vehicleinsurance.dto.PolicyProposalDto;
import com.vehicleinsurance.entity.Policy;
import com.vehicleinsurance.entity.PolicyStatus;
import com.vehicleinsurance.entity.User;
import com.vehicleinsurance.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/policies")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Policy Management", description = "Policy management APIs")
public class PolicyController {
    
    private final PolicyService policyService;
    
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }
    
    @PostMapping("/proposal")
    @Operation(summary = "Submit a new policy proposal", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Map<String, Object>> submitProposal(
            @Valid @RequestBody PolicyProposalDto proposalDto,
            Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Policy policy = policyService.createPolicyProposal(user, proposalDto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Policy proposal submitted successfully");
            response.put("policyNumber", policy.getPolicyNumber());
            response.put("premiumAmount", policy.getPremiumAmount());
            response.put("status", policy.getStatus());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/my-policies")
    @Operation(summary = "Get user's policies")
    public ResponseEntity<List<Policy>> getUserPolicies(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Policy> policies = policyService.getUserPolicies(user);
        return ResponseEntity.ok(policies);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get policy by ID")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Long id) {
        try {
            Policy policy = policyService.findById(id);
            return ResponseEntity.ok(policy);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Get policies by status (Officer only)")
    public ResponseEntity<List<Policy>> getPoliciesByStatus(@PathVariable PolicyStatus status) {
        List<Policy> policies = policyService.getPoliciesByStatus(status);
        return ResponseEntity.ok(policies);
    }
    
    @PutMapping("/{id}/status")
    @Operation(summary = "Update policy status (Officer only)")
    public ResponseEntity<Map<String, Object>> updatePolicyStatus(
            @PathVariable Long id,
            @RequestParam PolicyStatus status,
            Authentication authentication) {
        try {
            User officer = (User) authentication.getPrincipal();
            Policy updatedPolicy = policyService.updatePolicyStatus(id, status, officer);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Policy status updated successfully");
            response.put("policyNumber", updatedPolicy.getPolicyNumber());
            response.put("status", updatedPolicy.getStatus());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @GetMapping("/statistics")
    @Operation(summary = "Get policy statistics")
    public ResponseEntity<Map<String, Object>> getPolicyStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activePolicies", policyService.getActivePoliciesCount());
        return ResponseEntity.ok(stats);
    }
}
