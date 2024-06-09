package com.odaat.odaat.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.dao.request.CategoryRequest;
import com.odaat.odaat.dao.request.ProjectRequest;
import com.odaat.odaat.dao.response.CategoryResponse;
import com.odaat.odaat.dao.response.ProjectResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.service.ProjectService;
import com.odaat.odaat.service.SecurityService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SecurityService securityService;

    @GetMapping
    public List<ProjectResponse> getAllProjects() {
        return projectService.findAll().stream()
                .map(this::convertToDao)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Integer id) {
        Optional<Project> project = projectService.findById(id);
        if (!project.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDao(project.get()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        Project project = convertToEntity(projectRequest);
        Project savedProject = projectService.save(project);
        return ResponseEntity.ok(convertToDao(savedProject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequest projectRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if (!projectService.existsById(id)) {
            return ResponseEntity.badRequest().build();
            
        } else {
            Project project = convertToEntity(projectRequest);
            project.setId(id);
            Project savedCategory = projectService.save(project);
            return ResponseEntity.ok(convertToDao(savedCategory));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DAO CONVERTERS
    private ProjectResponse convertToDao(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }

    private Project convertToEntity(ProjectRequest projectRequest) {
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        Category category = new Category();
        category.setId(projectRequest.getCategoryId());
        project.setCategory(category);
        project.setUzer(securityService.getCurrentUser());

        return project;
    }
}
