package com.odaat.odaat.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

import com.odaat.odaat.model.enums.Source;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "uzer")
@SQLRestriction(value = "is_deleted = false")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Uzer extends BaseModel {

    @Id
    private String id;

    private String name;

    private String email;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    @PreUpdate
    public void onCreateUpdate(){
        this.isDeleted = false;
    }

}