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
import com.odaat.odaat.service.SyncService;
import com.odaat.odaat.service.TaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    // TODO: return only tasks for the user

    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthService authService;
    @Autowired
    private SyncController syncController;

    @GetMapping("/get")
    public List<TaskResponse> getAllTasks(
        @RequestParam(value = "projectId", required = false) Integer projectId,
        @RequestParam(value = "date", required = false) LocalDate date
    ) {

        return taskService.findAll(projectId, date).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Integer id) {
        Optional<Task> task = taskService.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDto(task.get()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        Task task = convertToEntity(taskRequest);
        Task savedTask = taskService.save(task);
        return ResponseEntity.ok(convertToDto(savedTask));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @Valid @RequestBody TaskRequest taskRequest, BindingResult bindingResult) {
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

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<?> updateTask(
        @PathVariable Integer id, @RequestParam TaskStatus status
    ) {

        Optional<Task> taskOptional = taskService.findById(id);
        
        if (taskOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
            
        } else {
            Task task = taskOptional.get();
            task.setStatus(status);
            task = taskService.save(task);

            // If the task is from an external app, it is synced if applicable.
            if(task.getSyncId() != null){
                syncController.syncTask(task);
            }

            return ResponseEntity.ok(convertToDto(task));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO and Entity Conversion
    public TaskResponse convertToDto(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(task, taskResponse);
        ProjectResponse projectResponse = new ProjectResponse();
        BeanUtils.copyProperties(task.getProject(), projectResponse);
        taskResponse.setProject(projectResponse);
        System.out.println(taskResponse);
        return taskResponse;
    }

    public Task convertToEntity(TaskRequest taskRequest) {

        Task task = new Task();
        BeanUtils.copyProperties(taskRequest, task);
        task.setUzer(authService.getCurrentUser());
        Project project = new Project();
        project.setId(taskRequest.getProjectId());
        task.setProject(project);
        return task;
    }
}