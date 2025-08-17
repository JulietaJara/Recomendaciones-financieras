package com.financialrecommendations.service;

import com.financialrecommendations.dto.UserRegistrationDto;
import com.financialrecommendations.dto.UserProfileDto;
import com.financialrecommendations.entity.User;
import com.financialrecommendations.entity.UserProfile;
import com.financialrecommendations.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setDateOfBirth(registrationDto.getDateOfBirth());
        user.setPhone(registrationDto.getPhone());
        
        return userRepository.save(user);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByEmailWithProfile(String email) {
        return userRepository.findByEmailWithProfile(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public User updateUserProfile(Long userId, UserProfileDto profileDto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            user.setUserProfile(profile);
        }
        
        profile.setRiskTolerance(profileDto.getRiskTolerance());
        profile.setInvestmentExperience(profileDto.getInvestmentExperience());
        profile.setInvestmentHorizon(profileDto.getInvestmentHorizon());
        profile.setMonthlyIncome(profileDto.getMonthlyIncome());
        profile.setMonthlyExpenses(profileDto.getMonthlyExpenses());
        profile.setCurrentSavings(profileDto.getCurrentSavings());
        profile.setInvestmentGoals(profileDto.getInvestmentGoals());
        profile.setAge(profileDto.getAge());
        profile.setEmploymentStatus(profileDto.getEmploymentStatus());
        
        return userRepository.save(user);
    }
}
