package com.odaat.odaat.dao.request;

import java.time.LocalDateTime;

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
    private Integer projectId;

    private String description;
    private TaskStatus status;
    private Priority priority;
    private LocalDateTime startTime;
    private Double durationHr;

}