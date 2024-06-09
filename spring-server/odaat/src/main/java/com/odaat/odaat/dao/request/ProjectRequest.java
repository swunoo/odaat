package com.odaat.odaat.dao.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {

    @NotNull
    private Integer category_id;

    private String name;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dueTime;
    private Double estimatedHr;
    private Double dailyHr;
}