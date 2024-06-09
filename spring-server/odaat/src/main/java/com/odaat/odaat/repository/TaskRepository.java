package com.odaat.odaat.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t " +
           "WHERE (:projectId IS NULL OR t.project.id = :projectId) " +
           "AND (:date IS NULL OR t.startTime = :date)")
    List<Task> find(
        @Param("projectId") Integer projectId,
        @Param("date") LocalDateTime date
    );
}
