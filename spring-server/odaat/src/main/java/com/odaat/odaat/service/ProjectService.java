package com.odaat.odaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;
import com.odaat.odaat.repository.ProjectRepository;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/*
    Service layer for the "Project" entity.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskService taskService;

    public List<Project> findAll(String userId) {
        return projectRepository.findByUzerId(userId);
    }

    public Integer countProjects(String userId){
        return projectRepository.countByUserId(userId);
    }

    public Optional<Project> findById(Integer id) {
        return projectRepository.findById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project saveDefault(Uzer uzer, Category category) {

        Project project = new Project();
        project.setUzer(uzer);
        project.setCategory(category);
        project.setName("Sample Project");
        project.setDescription("Sample project description");
        project.setStatus(ProjectStatus.CREATED);
        project.setPriority(Priority.LOWEST);

        return projectRepository.save(project);
    }

    public void deleteById(Integer id) {
        taskService.deleteAllByProjectId(id);
        projectRepository.softDelete(id);
    }

    public boolean existsById(Integer id) {
        return projectRepository.existsById(id);
    }

    public Optional<Project> getProjectBySyncId(Integer syncId, String userId){
        return projectRepository.getBySyncId(syncId, userId);
    }

}
