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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.odaat.odaat.model.Task;
import com.odaat.odaat.repository.TaskRepository;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Task task = new Task();
        when(taskRepository.findByProjectId(null)).thenReturn(Collections.singletonList(task));

        List<Task> tasks = taskService.findAll(null, null);
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findByProjectId(null);
    }

    @Test
    void testFindById() {
        Task task = new Task();
        task.setId(1);
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        Optional<Task> foundTask = taskService.findById(1);
        assertTrue(foundTask.isPresent());
        assertEquals(1, foundTask.get().getId());
        verify(taskRepository, times(1)).findById(anyInt());
    }

    @Test
    void testSave() {
        Task task = new Task();
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.save(task);
        assertNotNull(savedTask);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(taskRepository).deleteById(anyInt());

        taskService.deleteById(1);
        verify(taskRepository, times(1)).deleteById(anyInt());
    }
}
