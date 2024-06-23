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
import org.mockito.junit.jupiter.MockitoExtension;

import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.TaskStatus;

@ExtendWith(MockitoExtension.class)
public class LocalSyncServiceTest {

    @Mock
    private AuthService authService;
    
    @Mock
    private CategoryService categoryService;
    
    @Mock
    private ProjectService projectService;
    
    @Mock
    private TaskService taskService;
    
    @InjectMocks
    private LocalSyncService localSync;
        
    @Test
    void testUpdateLocalProjects_existingProject() {
        ProjectIdAndName externalProject = new ProjectIdAndName();
        externalProject.setId(1);
        externalProject.setName("External Project");
        
        Project existingProject = new Project();
        existingProject.setSyncId(1);
        existingProject.setName("Old Project Name");
        
        when(authService.getCurrentUserId()).thenReturn("1");
        when(projectService.getProjectBySyncId(externalProject.getId(), "1")).thenReturn(Optional.of(existingProject));
        
        localSync.updateLocalProjects(List.of(externalProject));
        
        assertEquals("External Project", existingProject.getName());
        verify(projectService).save(existingProject);
    }
    
    @Test
    void testUpdateLocalProjects_newProject() {
        Uzer uzer = new Uzer();
        uzer.setId("1");
        
        ProjectIdAndName externalProject = new ProjectIdAndName();
        externalProject.setId(2);
        externalProject.setName("New Project");

        when(authService.getCurrentUserId()).thenReturn("1");
        when(projectService.getProjectBySyncId(externalProject.getId(), "1")).thenReturn(Optional.empty());
        when(authService.getCurrentUser()).thenReturn(uzer);
        when(categoryService.getDefaultCategory("1")).thenReturn(new Category());
        
        localSync.updateLocalProjects(List.of(externalProject));
        
        verify(projectService).save(any(Project.class));
    }
    
    @Test
    void testUpdateLocalTasks() {
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
        
        when(authService.getCurrentUserId()).thenReturn("1");
        when(projectService.getProjectBySyncId(issue.getProjectId(), "1")).thenReturn(Optional.of(project));
        when(taskService.getTasksByProjectIdAndSyncId(project.getId(), issue.getId())).thenReturn(List.of(task1, task2, task3));
        
        localSync.updateLocalTasks(List.of(issue));
        
        verify(taskService, times(2)).save(any(Task.class));
        verify(taskService, never()).saveAll(any());
    }
}
