package com.back.service;

import com.back.dto.ProfileDto;
import com.back.entity.Profile;
import com.back.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile save(Profile profile) {
        Profile pro = profileRepository.findByUserId(profile.getUserId());
        if (pro != null) {
            new RuntimeException("Profile already exists");
        }
        return profileRepository.save(profile);
    }
    public Profile findByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    public Profile findPublicProfile() {
        return profileRepository.findAll()
                .stream()
                .filter(profile -> profile.getFullName() != null && !profile.getFullName().isBlank())
                .findFirst()
                .orElse(null);
    }

}
