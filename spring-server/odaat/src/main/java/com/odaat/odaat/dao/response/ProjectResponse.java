package com.odaat.odaat.dao.response;

import java.time.LocalDateTime;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Integer id;
    private CategoryResponse category;
    private String name;
    private String description;
    private ProjectStatus status;
    private Priority priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dueTime;
    private Double estimatedHr;
    private Double dailyHr;
    private LocalDateTime createdAt;    
}
