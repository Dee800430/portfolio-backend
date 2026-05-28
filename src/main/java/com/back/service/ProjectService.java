package com.back.service;

import com.back.entity.Contact;
import com.back.entity.Project;
import com.back.repository.ProjectRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    public Project updateProject(Project project, Long id) {
        Project oldProject = projectRepository.findById(id).orElseThrow(()-> new RuntimeException("Project not found"));
        return projectRepository.save(oldProject);
    }
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}
