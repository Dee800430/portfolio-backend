package com.back.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String mobile;

    @Column(length = 5000)
    private String about;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "profile_skills",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "skills")
    private List<String> skills;

    @Column(length = 5000)
    private String experience;

    private String resumeUrl;

    private String githubUrl;

    private String linkedinUrl;

    private String profileImage;

    private Long userId;
}
