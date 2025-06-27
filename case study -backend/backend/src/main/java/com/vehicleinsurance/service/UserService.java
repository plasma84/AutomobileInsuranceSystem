package com.vehicleinsurance.service;

import com.vehicleinsurance.dto.UserRegistrationDto;
import com.vehicleinsurance.entity.User;
import com.vehicleinsurance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User registerUser(UserRegistrationDto registrationDto) {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email is already registered");
        }
        
        if (userRepository.existsByAadhaarNumber(registrationDto.getAadhaarNumber())) {
            throw new RuntimeException("Aadhaar number is already registered");
        }
        
        if (userRepository.existsByPanNumber(registrationDto.getPanNumber())) {
            throw new RuntimeException("PAN number is already registered");
        }
        
        // Create new user
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFullName(registrationDto.getFullName());
        user.setDateOfBirth(registrationDto.getDateOfBirth());
        user.setAddress(registrationDto.getAddress());
        user.setAadhaarNumber(registrationDto.getAadhaarNumber());
        user.setPanNumber(registrationDto.getPanNumber());
        
        return userRepository.save(user);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
