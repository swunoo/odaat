package com.odaat.odaat.dto;

import com.odaat.odaat.model.enums.Priority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BacklogIssue {
    Integer id;
    Integer projectId;
    String summary;
    Priority priority;
    Double remainingHours;
}
