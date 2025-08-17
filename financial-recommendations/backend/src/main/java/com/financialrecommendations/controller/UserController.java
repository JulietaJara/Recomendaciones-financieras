package com.financialrecommendations.controller;

import com.financialrecommendations.dto.UserProfileDto;
import com.financialrecommendations.entity.User;
import com.financialrecommendations.service.UserService;
import com.financialrecommendations.security.CurrentUser;
import com.financialrecommendations.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userService.findByEmailWithProfile(currentUser.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> updateProfile(@CurrentUser UserPrincipal currentUser,
                                            @Valid @RequestBody UserProfileDto profileDto) {
        try {
            User updatedUser = userService.updateUserProfile(currentUser.getId(), profileDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
