package com.odaat.odaat.dto.response;

import java.time.LocalDateTime;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    
    private Integer id;
    private ProjectResponse project;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime startTime;
    private Double durationHr;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
