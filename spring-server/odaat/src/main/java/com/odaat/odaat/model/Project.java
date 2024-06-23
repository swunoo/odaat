package com.odaat.odaat.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;
import com.odaat.odaat.model.enums.Source;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class Project extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "uzer_id", nullable = false)
    private Uzer uzer;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    private Integer syncId; // Id of the external project (e.g. Backlog's project id)

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Source source;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime dueTime;
    private Double estimatedHr;
    private Double dailyHr;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void onCreateUpdate(){
        this.isDeleted = false;
        if(this.source == null) this.source = Source.NATIVE;
    }

}
