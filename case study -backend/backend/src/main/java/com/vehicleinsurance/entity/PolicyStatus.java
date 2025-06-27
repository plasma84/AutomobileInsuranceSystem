package com.vehicleinsurance.entity;

public enum PolicyStatus {
    PROPOSAL_SUBMITTED("Proposal Submitted"),
    UNDER_REVIEW("Under Review"),
    ADDITIONAL_INFO_REQUIRED("Additional Information Required"),
    QUOTE_GENERATED("Quote Generated"),
    PAYMENT_PENDING("Payment Pending"),
    ACTIVE("Active"),
    EXPIRED("Expired"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");
    
    private final String displayName;
    
    PolicyStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
