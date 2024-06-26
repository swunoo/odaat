package com.odaat.odaat.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Task;
import com.odaat.odaat.repository.TaskRepository;

/*
    Service layer for the "Task" entity.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    public List<Task> findAll(Integer projectId, LocalDate date, String currentUserId){
        if(date != null){
            return taskRepository.findByDate(date.atTime(LocalTime.MIN), date.atTime(LocalTime.MAX), currentUserId);
        }
        return taskRepository.findByProjectId(projectId, currentUserId);
    }
        
    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> saveAll(List<Task> tasks) {
        return taskRepository.saveAll(tasks);
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

    public List<Task> getTasksByProjectIdAndSyncId(Integer projectId, Integer syncId){
        return taskRepository.findByProjectIdAndSyncId(projectId, syncId);
    }

    public Double getTotalHoursSpent(Integer projectId, Integer syncId){
        return taskRepository.getTotalHoursSpent(projectId, syncId);
    }

}
