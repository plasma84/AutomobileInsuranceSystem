package com.vehicleinsurance.entity;

public enum CoverageType {
    BASIC("Basic Coverage"),
    COMPREHENSIVE("Comprehensive Coverage"),
    PREMIUM("Premium Coverage");
    
    private final String displayName;
    
    CoverageType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
