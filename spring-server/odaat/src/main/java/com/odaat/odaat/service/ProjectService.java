package com.odaat.odaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Project;
import com.odaat.odaat.repository.ProjectRepository;

/*
    Service layer for the "Project" entity.
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskService taskService;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Integer id) {
        return projectRepository.findById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public void deleteById(Integer id) {
        taskService.deleteAllByProjectId(id);
        projectRepository.softDelete(id);
    }

    public boolean existsById(Integer id) {
        return projectRepository.existsById(id);
    }

    public Optional<Project> getProjectBySyncId(Integer id){
        return projectRepository.getBySyncId(id);
    }

}
