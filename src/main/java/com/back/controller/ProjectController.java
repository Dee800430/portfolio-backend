package com.back.controller;

import com.back.entity.Project;
import com.back.entity.User;
import com.back.repository.UserRepo;
import com.back.service.CloudinaryService;
import com.back.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    private  CloudinaryService cloudinaryService;

    private final UserRepo userRepo;


    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.findAllProjects();
    }
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.findById(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
            projectService.deleteProject(id);
            return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> saveProject(

            @RequestParam(required = false) Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String techStack,
            @RequestParam String githubUrl,
            @RequestParam String liveDemoUrl,
            @RequestParam String category,
            @RequestParam boolean featured,
            @RequestParam(required = false) MultipartFile image,
            Authentication authentication

    ) {

        Long userId = getUserId(authentication);

        Project project;

        // UPDATE
        if (id != null) {

            project = projectService.findById(id);

            if (project == null) {
                return ResponseEntity
                        .badRequest()
                        .body("Project not found");
            }

        } else {

            // CREATE
            project = new Project();
        }

        // Upload image only if provided
        if (image != null && !image.isEmpty()) {

            String imageUrl = cloudinaryService.uploadFile(image);
            project.setImageUrl(imageUrl);
        }

        project.setTitle(title);
        project.setDescription(description);
        project.setTechStack(techStack);
        project.setGithubUrl(githubUrl);
        project.setLiveDemoUrl(liveDemoUrl);
        project.setCategory(category);
        project.setFeatured(featured);
        project.setUserId(userId);

        projectService.saveProject(project);

        return ResponseEntity.ok(
                id == null
                        ? "Project created successfully"
                        : "Project updated successfully"
        );
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
