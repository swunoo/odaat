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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.odaat.odaat.model.Task;
import com.odaat.odaat.repository.TaskRepository;
import com.odaat.odaat.utils.MockUtil;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testFindByProject() {
        Task task = MockUtil.mockInstance(Task.class);
        Integer projectId = task.getProject().getId();
        when(taskRepository.findByProjectId(projectId, "1")).thenReturn(List.of(task));

        List<Task> tasks = taskService.findAll(projectId, null, "1");
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findByProjectId(projectId, "1");
    }

    @Test
    void testFindByDate() {
        Task task = MockUtil.mockInstance(Task.class);
        LocalDate date = task.getStartTime().toLocalDate();
        LocalDateTime start = date.atTime(LocalTime.MIN);
        LocalDateTime end = date.atTime(LocalTime.MAX);
        when(taskRepository.findByDate(start, end, "1")).thenReturn(List.of(task));

        List<Task> tasks = taskService.findAll(null, date, "1");
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findByDate(start, end, "1");
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
