package com.back.controller;

import com.back.entity.Profile;
import com.back.service.CloudinaryService;
import com.back.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    private final CloudinaryService cloudinaryService;


    @GetMapping
    public Profile getProfile(Authentication authentication) {
        return profileService.findByUserId(getUserId(authentication));

    }

    @GetMapping("/public")
    public Profile getPublicProfile() {
        return profileService.findPublicProfile();
    }

    @PutMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> updateProfile(

            @RequestParam String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String mobile,
            @RequestParam String about,
            @RequestParam List<String> skills,
            @RequestParam String experience,
            @RequestParam String githubUrl,
            @RequestParam String linkedinUrl,

            @RequestParam(required = false) MultipartFile profileImage,
            @RequestParam(required = false) MultipartFile resume,

            Authentication authentication
    ) {

        Long userId = getUserId(authentication);



        Profile profile = profileService.findByUserId(userId);

        if (profile == null) {
            return ResponseEntity.badRequest().body("Profile not found");
        }

        profile.setFullName(fullName);
        profile.setEmail(email);
        profile.setMobile(mobile);
        profile.setAbout(about);
        profile.setSkills(skills);

        profile.setExperience(experience);
        profile.setGithubUrl(githubUrl);
        profile.setLinkedinUrl(linkedinUrl);

        // Update image only if provided
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFile(profileImage);
            profile.setProfileImage(imageUrl);
        }

        // Update resume only if provided
        if (resume != null && !resume.isEmpty()) {
            String resumeUrl = cloudinaryService.uploadFile(resume);
            profile.setResumeUrl(resumeUrl);
        }

        profileService.save(profile);

        return ResponseEntity.ok("Profile updated successfully");
    }

    private Long getUserId(Authentication authentication) {

        if (authentication == null || !(authentication.getCredentials() instanceof Map<?, ?> claims)) {
            throw new RuntimeException("Authenticated userId not found");
        }

        Object userId = claims.get("userId");

        if (userId instanceof Long id) {
            return id;
        }

        if (userId instanceof Number id) {
            return id.longValue();
        }

        throw new RuntimeException("Authenticated userId not found");
    }
}
