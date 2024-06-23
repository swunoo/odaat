package com.odaat.odaat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.model.enums.Source;
import com.odaat.odaat.model.enums.TaskStatus;

/* Update local data based on the external data. */
@Service
public class LocalSyncService {
    @Autowired AuthService authService;
    @Autowired CategoryService categoryService;
    @Autowired ProjectService projectService;
    @Autowired TaskService taskService;

    // Task duration is assumed to be 2 hours by default
    private double DEFAULT_TASK_DURATION = 2.0;

    // Sync external projects into the database
    public void updateLocalProjects(List<ProjectIdAndName> externalProjects){
        Optional<Project> project;
        for(ProjectIdAndName externalProject : externalProjects){
            project = projectService.getProjectBySyncId(externalProject.getId());
            // If project exists, we check the name and update if the name has changed.
            if(project.isPresent()){
                Project existingProject = project.get();
                if(existingProject.getName() == null || !existingProject.getName().equals(externalProject.getName())){
                    existingProject.setName(externalProject.getName());
                    projectService.save(existingProject);
                }

            // If project doesn't exist, we create it and save it.
            } else {
                Project newProject = new Project();
                newProject.setUzer(authService.getCurrentUser());
                newProject.setSyncId(externalProject.getId());
                newProject.setName(externalProject.getName());
                newProject.setSource(Source.BACKLOG);
                newProject.setCategory(categoryService.getDefaultCategory());
                projectService.save(newProject);
            }
        }
    }

    // Sync external tasks into the database
    public void updateLocalTasks(List<BacklogIssue> issues){

        Project project;
        List<Task> tasks;
        // For each issue,
        for (BacklogIssue issue : issues) {

            // Extract its related tasks
            // 1. get the related project
            project = projectService.getProjectBySyncId(issue.getProjectId()).orElseThrow();
            // 2. use the projectId and issueId to get tasks
            tasks = taskService.getTasksByProjectIdAndSyncId(project.getId(), issue.getId());
            // 3. Adjust the balance between incompleted tasks' hour and issue's hour
            // 3.1. Set tasks to completed as necessary
            for(Task task : tasks){
                if(task.getStatus().equals(TaskStatus.COMPLETED)) continue;
                Double remainingHrs = issue.getRemainingHours();
                if(remainingHrs == 0){
                    task.setStatus(TaskStatus.COMPLETED);
                    taskService.save(task);
                } else {
                    issue.setRemainingHours(Math.max(0, remainingHrs - task.getDurationHr()));
                }
            }
            // 3.2. If remainingHrs are not completely covered, more tasks are added
            int tasksToAdd = (int) Math.ceil(issue.getRemainingHours() / DEFAULT_TASK_DURATION);
            if(tasksToAdd > 0){
                List<Task> newTasks = new ArrayList<>();
                for(int i = 0; i<tasksToAdd; i++){
                    Task task = new Task();
                    task.setUzer(authService.getCurrentUser());
                    task.setProject(project);
                    task.setSyncId(issue.getId());
                    task.setDescription(issue.getSummary() + " " + i);
                    task.setStatus(TaskStatus.GENERATED);
                    task.setPriority(issue.getPriority());
                    task.setDurationHr(DEFAULT_TASK_DURATION);
                    newTasks.add(task);
                }
                taskService.saveAll(newTasks);
            }
        }
    }
}