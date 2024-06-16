package com.odaat.odaat.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.dto.request.ProjectRequest;
import com.odaat.odaat.dto.response.ProjectResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.service.ProjectService;
import com.odaat.odaat.service.AuthService;

@RestController
@RequestMapping("/api/project")
@Validated
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private AuthService authService;

    @GetMapping("/get")
    public List<ProjectResponse> getAllProjects() {
        return projectService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/getIdName")
    public List<ProjectIdAndName> getProjectIdAndNames() {
        return projectService.findAll().stream()
                .map(project -> new ProjectIdAndName(project.getId(), project.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Integer id) {
        Optional<Project> project = projectService.findById(id);
        if (!project.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDto(project.get()));
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest projectRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        Project project = convertToEntity(projectRequest);
        Project savedProject = projectService.save(project);
        return ResponseEntity.ok(convertToDto(savedProject));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id, @Valid @RequestBody ProjectRequest projectRequest, BindingResult bindingResult) {
        // TODO: Improve validation
        if (bindingResult.hasErrors() || projectRequest.getName().length() > 32) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if (!projectService.existsById(id)) {
            return ResponseEntity.badRequest().build();
            
        } else {
            Project project = convertToEntity(projectRequest);
            project.setId(id);
            Project savedCategory = projectService.save(project);
            return ResponseEntity.ok(convertToDto(savedCategory));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Integer id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO and Entity Conversion
    public ProjectResponse convertToDto(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(project, projectResponse);
        return projectResponse;
    }

    public Project convertToEntity(ProjectRequest projectRequest) {
        Project project = new Project();
        BeanUtils.copyProperties(projectRequest, project);
        Category category = new Category();
        category.setId(projectRequest.getCategoryId());
        project.setCategory(category);
        project.setUzer(authService.getCurrentUser());

        return project;
    }
}
