package com.odaat.odaat.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.dto.request.TaskRequest;
import com.odaat.odaat.dto.response.ProjectResponse;
import com.odaat.odaat.dto.response.TaskResponse;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.model.enums.TaskStatus;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.BacklogSyncService;
import com.odaat.odaat.service.TaskService;

/*
   Controller for the "Task" entity.
   1. Endpoints
   - Get all, Get One, Create, Update, Update status, Delete.
   
   2. DTO and Entity conversion
   - Entity -> DTO, DTO -> Entity
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BacklogSyncService backlogSync;

    /* 1. Endpoints */

    @GetMapping("/get") // Get all by date and/or project
    public List<TaskResponse> getAllTasks(
            @RequestParam(value = "projectId", required = false) Integer projectId,
            @RequestParam(value = "date", required = false) LocalDate date) {

        String userId = authService.getCurrentUserId();

        return taskService.findAll(projectId, date, userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/detail/{id}") // Get one
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        Optional<Task> task = taskService.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDto(task.get()));
        }
    }

    @PostMapping("/add") // Create
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Task task = convertToEntity(taskRequest);
        Task savedTask = taskService.save(task);
        return ResponseEntity.ok(convertToDto(savedTask));
    }

    @PutMapping("/update/{id}") // Update
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @Valid @RequestBody TaskRequest taskRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        if (!taskService.existsById(id)) {
            return ResponseEntity.badRequest().build();

        } else {
            Task task = convertToEntity(taskRequest);
            task.setId(id);
            Task savedTask = taskService.save(task);
            return ResponseEntity.ok(convertToDto(savedTask));
        }
    }

    @PutMapping("/updateStatus/{id}") // Update completion status
    public ResponseEntity<?> updateTask(
            @PathVariable Integer id, @RequestParam TaskStatus status) {

        Optional<Task> taskOptional = taskService.findById(id);

        if (taskOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();

        } else {
            Task task = taskOptional.get();
            task.setStatus(status);
            task = taskService.save(task);

            // If the task is from an external app, it is synced if applicable.
            if (task.getSyncId() != null) {
                try {
                    backlogSync.syncTaskToBacklog(task);
                } catch (Exception e) {
                    System.out.println("Couldn't sync task to Backlog.");
                    e.printStackTrace();
                }
            }

            return ResponseEntity.ok(convertToDto(task));
        }
    }

    @DeleteMapping("/delete/{id}") // Delete
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /* 2. DTO and Entity conversion */

    public TaskResponse convertToDto(Task task) { // Task to TaskResponse
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(task, taskResponse);
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(task.getProject(), projectResponse);
        taskResponse.setProject(projectResponse);
        System.out.println(taskResponse);
        return taskResponse;
    }

    public Task convertToEntity(TaskRequest taskRequest) { // TaskRequest to Task
        Task task = new Task();
        BeanUtils.copyProperties(taskRequest, task);
        task.setUzer(authService.getCurrentUser());
        Project project = new Project();
        project.setId(taskRequest.getProjectId());
        task.setProject(project);
        return task;
    }
}