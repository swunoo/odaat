package com.odaat.odaat.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> findAll(Integer projectId, LocalDate date){
        if(date != null){
            return taskRepository.findByDate(date.atTime(LocalTime.MIN), date.atTime(LocalTime.MAX));
        }
        return taskRepository.findByProjectId(projectId);
    }
        
    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Integer id) {
        taskRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return taskRepository.existsById(id);
    }

    public void deleteAllByProjectId(Integer projectId) {
        taskRepository.deleteAllByProjectId(projectId);
    }

    public void syncTasks(List<BacklogIssue> issues){
        System.out.println(issues);
    }
}
