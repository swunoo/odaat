package com.odaat.odaat.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @NotNull
    @Min(1)
    private Integer projectId;

    private String description;
    private TaskStatus status;
    private Priority priority;

    @NotNull
    private LocalDateTime startTime;
    private Double durationHr;

}