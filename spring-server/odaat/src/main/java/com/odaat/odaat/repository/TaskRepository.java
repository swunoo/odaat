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

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    List<Task> findByProjectId(
        @Param("projectId") Integer projectId
    );

    @Query("SELECT t FROM Task t WHERE t.startTime <= :dayEnd AND t.startTime >= :dayStart")
    List<Task> findByDate(
        @Param("dayStart") LocalDateTime dayStart,
        @Param("dayEnd") LocalDateTime dayEnd
    );
}
