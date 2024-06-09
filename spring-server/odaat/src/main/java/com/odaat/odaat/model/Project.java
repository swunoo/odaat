package com.odaat.odaat.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project")
@SQLRestriction(value = "is_deleted = false")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "uzer_id", nullable = false)
    private Uzer uzer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dueTime;
    private Double estimatedHr;
    private Double dailyHr;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    
}
