package com.back.controller;

import com.back.conifig.JwtFilter;
import com.back.conifig.JwtService;
import com.back.dto.AuthRequest;
import com.back.dto.AuthResponse;
import com.back.entity.Profile;
import com.back.entity.User;
import com.back.repository.ProfileRepository;
import com.back.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private  final PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;
    private final ProfileRepository profileRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);
        Profile profile = new Profile();
           profile.setUserId(savedUser.getUserId());
           profileRepository.save(profile);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token =
                jwtService.generateToken(
                        user
                );

        return new AuthResponse(token);
    }
}
