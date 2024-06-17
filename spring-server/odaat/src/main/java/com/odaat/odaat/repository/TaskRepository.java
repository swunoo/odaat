package com.odaat.odaat.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Task;

import jakarta.transaction.Transactional;

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

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.syncId = :syncId ORDER BY t.startTime DESC")
    List<Task> findByProjectIdAndSyncId(
        @Param("projectId") Integer projectId,
        @Param("syncId") Integer syncId
    );

    @Query("SELECT SUM(t.durationHr) FROM Task t WHERE t.status = 'COMPLETED' AND t.project.id = :projectId AND t.syncId = :syncId")
    Double getTotalHoursSpent(
        @Param("projectId") Integer projectId,
        @Param("syncId") Integer syncId
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.project.id = :projectId")
    void deleteAllByProjectId(@Param("projectId") Integer projectId);
}
