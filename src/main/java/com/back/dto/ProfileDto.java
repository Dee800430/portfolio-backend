package com.back.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileDto {

    private String fullName;

    private String about;

    private List<String> skills;

    private String experience;

    private String resumeUrl;

    private String githubUrl;

    private String linkedinUrl;

    private String profileImage;
}