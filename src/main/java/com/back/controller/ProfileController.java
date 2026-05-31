package com.back.controller;

import com.back.entity.Profile;
import com.back.entity.User;
import com.back.service.CloudinaryService;
import com.back.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import com.back.repository.UserRepo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    private  final UserRepo userRepo;


    @GetMapping
    public Profile getProfile(Authentication authentication) {
        return profileService.findByUserId(getUserId(authentication));

    }

    @GetMapping("/public")
    public Profile getPublicProfile(Authentication authentication) {
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

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String username = userDetails.getUsername();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return user.getUserId();
    }
}
