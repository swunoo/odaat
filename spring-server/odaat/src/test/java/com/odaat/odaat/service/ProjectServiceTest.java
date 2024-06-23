package com.odaat.odaat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.odaat.odaat.model.Project;
import com.odaat.odaat.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void testFindAll() {
        Project project = new Project();
        when(projectRepository.findByUzerId("1")).thenReturn(Collections.singletonList(project));

        List<Project> projects = projectService.findAll("1");
        assertEquals(1, projects.size());
        verify(projectRepository, times(1)).findByUzerId("1");
    }

    @Test
    void testFindById() {
        Project project = new Project();
        project.setId(1);
        when(projectRepository.findById(anyInt())).thenReturn(Optional.of(project));

        Optional<Project> foundProject = projectService.findById(1);
        assertTrue(foundProject.isPresent());
        assertEquals(1, foundProject.get().getId());
        verify(projectRepository, times(1)).findById(anyInt());
    }

    @Test
    void testSave() {
        Project project = new Project();
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project savedProject = projectService.save(project);
        assertNotNull(savedProject);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(projectRepository).softDelete(1);
        doNothing().when(taskService).deleteAllByProjectId(1);

        projectService.deleteById(1);
        verify(projectRepository, times(1)).softDelete(1);
        verify(taskService, times(1)).deleteAllByProjectId(1);
    }
}
