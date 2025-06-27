package com.vehicleinsurance.controller;

import com.vehicleinsurance.dto.ClaimDto;
import com.vehicleinsurance.entity.Claim;
import com.vehicleinsurance.entity.ClaimStatus;
import com.vehicleinsurance.entity.User;
import com.vehicleinsurance.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/claims")
@Tag(name = "Claims", description = "Claim management operations")
public class ClaimController {

    private static final String ERROR_PREFIX = "Error: ";
    private static final String MESSAGE_KEY = "message";
    private static final String ERROR_KEY = "error";
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @Operation(summary = "Create a new claim")
    public ResponseEntity<Map<String, Object>> createClaim(@Valid @RequestBody ClaimDto claimDto, 
                                        Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            
            Claim claim = claimService.createClaim(claimDto, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "Claim created successfully");
            response.put("claimNumber", claim.getClaimNumber());
            response.put("claimId", claim.getId());
            response.put("status", claim.getStatus());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + "Failed to create claim");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/my-claims")
    @Operation(summary = "Get user's claims")
    public ResponseEntity<List<Claim>> getUserClaims(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        
        List<Claim> claims = claimService.getUserClaims(user);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get claim by ID")
    public ResponseEntity<Map<String, Object>> getClaimById(@PathVariable Long id, Authentication authentication) {
        Optional<Claim> claimOpt = claimService.getClaimById(id);
        if (claimOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Claim claim = claimOpt.get();
        User user = (User) authentication.getPrincipal();

        // Check if user owns the claim or is an officer/admin
        if (!claim.getPolicy().getUser().getId().equals(user.getId()) && 
            !user.getRole().name().equals("OFFICER") && 
            !user.getRole().name().equals("ADMIN")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, "You don't have permission to view this claim");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("claim", claim);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('OFFICER') or hasRole('ADMIN')")
    @Operation(summary = "Get claims by status (Officer/Admin only)")
    public ResponseEntity<List<Claim>> getClaimsByStatus(@PathVariable String status) {
        try {
            ClaimStatus claimStatus = ClaimStatus.valueOf(status.toUpperCase());
            List<Claim> claims = claimService.getClaimsByStatus(claimStatus);
            return ResponseEntity.ok(claims);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('OFFICER') or hasRole('ADMIN')")
    @Operation(summary = "Get all claims (Officer/Admin only)")
    public ResponseEntity<List<Claim>> getAllClaims() {
        List<Claim> claims = claimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('OFFICER') or hasRole('ADMIN')")
    @Operation(summary = "Update claim status (Officer/Admin only)")
    public ResponseEntity<Map<String, Object>> updateClaimStatus(@PathVariable Long id, 
                                              @RequestParam String status,
                                              Authentication authentication) {
        try {
            ClaimStatus newStatus = ClaimStatus.valueOf(status.toUpperCase());
            User user = (User) authentication.getPrincipal();
            
            Claim updatedClaim = claimService.updateClaimStatus(id, newStatus, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "Claim status updated successfully");
            response.put("claimNumber", updatedClaim.getClaimNumber());
            response.put("status", updatedClaim.getStatus());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + "Failed to update claim status");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete claim")
    public ResponseEntity<Map<String, Object>> deleteClaim(@PathVariable Long id, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            
            claimService.deleteClaim(id, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put(MESSAGE_KEY, "Claim deleted successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + "Failed to delete claim");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('OFFICER') or hasRole('ADMIN')")
    @Operation(summary = "Get claim statistics (Officer/Admin only)")
    public ResponseEntity<Map<String, Object>> getClaimStatistics() {
        try {
            long totalClaims = claimService.getTotalClaimsCount();
            long submittedClaims = claimService.getClaimsCountByStatus(ClaimStatus.SUBMITTED);
            long underReviewClaims = claimService.getClaimsCountByStatus(ClaimStatus.UNDER_REVIEW);
            long approvedClaims = claimService.getClaimsCountByStatus(ClaimStatus.APPROVED);
            long rejectedClaims = claimService.getClaimsCountByStatus(ClaimStatus.REJECTED);

            Map<String, Object> response = new HashMap<>();
            response.put("total", totalClaims);
            response.put("submitted", submittedClaims);
            response.put("underReview", underReviewClaims);
            response.put("approved", approvedClaims);
            response.put("rejected", rejectedClaims);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(ERROR_KEY, ERROR_PREFIX + "Failed to fetch claim statistics");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
