package com.odaat.odaat.dto.request;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

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
    @Min(1)
    private Integer categoryId;

    private String name;
    private String description;
    private ProjectStatus status;
    private Priority priority;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dueTime;
    private Double estimatedHr;
    private Double dailyHr;
}