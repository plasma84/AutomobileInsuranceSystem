package com.vehicleinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VehicleInsuranceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleInsuranceApplication.class, args);
    }
}
