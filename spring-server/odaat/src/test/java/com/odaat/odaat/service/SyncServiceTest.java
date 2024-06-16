package com.odaat.odaat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.TaskStatus;

public class SyncServiceTest {

    @Mock
    private AuthService authService;
    
    @Mock
    private CategoryService categoryService;
    
    @Mock
    private ProjectService projectService;
    
    @Mock
    private TaskService taskService;
    
    @InjectMocks
    private SyncService syncService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(authService.getCurrentUser()).thenReturn(new Uzer());
        when(categoryService.getDefaultCategory()).thenReturn(new Category());
    }
    
    @Test
    void testSyncTasks() {
        BacklogIssue issue = new BacklogIssue(1, 1, "Issue Summary", Priority.MEDIUM, 1.0);
        
        Project project = new Project();
        project.setId(1);
        
        Task task1 = new Task();
        task1.setDurationHr(2.0);
        task1.setStatus(TaskStatus.GENERATED);
        
        Task task2 = new Task();
        task2.setDurationHr(3.0);
        task2.setStatus(TaskStatus.PLANNED);
        
        Task task3 = new Task();
        task3.setDurationHr(1.0);
        task3.setStatus(TaskStatus.PLANNED);
        
        when(projectService.getProjectBySyncId(issue.getProjectId())).thenReturn(Optional.of(project));
        when(taskService.getTasksByProjectIdAndSyncId(project.getId(), issue.getId())).thenReturn(List.of(task1, task2, task3));
        
        syncService.syncTasks(List.of(issue));
        
        verify(taskService, times(2)).save(any(Task.class));
        verify(taskService, never()).saveAll(any());
    }
    
    @Test
    void testSyncProjects_existingProject() {
        ProjectIdAndName externalProject = new ProjectIdAndName();
        externalProject.setId(1);
        externalProject.setName("External Project");
        
        Project existingProject = new Project();
        existingProject.setSyncId(1);
        existingProject.setName("Old Project Name");
        
        when(projectService.getProjectBySyncId(externalProject.getId())).thenReturn(Optional.of(existingProject));
        
        syncService.syncProjects(List.of(externalProject));
        
        assertEquals("External Project", existingProject.getName());
        verify(projectService).save(existingProject);
    }
    
    @Test
    void testSyncProjects_newProject() {
        ProjectIdAndName externalProject = new ProjectIdAndName();
        externalProject.setId(2);
        externalProject.setName("New Project");
        
        when(projectService.getProjectBySyncId(externalProject.getId())).thenReturn(Optional.empty());
        
        syncService.syncProjects(List.of(externalProject));
        
        verify(projectService).save(any(Project.class));
    }
}
