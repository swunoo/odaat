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

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.uzer.id = :userId")
    List<Task> findByProjectId(
        @Param("projectId") Integer projectId,
        @Param("userId") String userId
    );

    @Query("SELECT t FROM Task t WHERE t.startTime <= :dayEnd AND t.startTime >= :dayStart AND t.uzer.id = :userId")
    List<Task> findByDate(
        @Param("dayStart") LocalDateTime dayStart,
        @Param("dayEnd") LocalDateTime dayEnd,
        @Param("userId") String userId
    );

    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.syncId = :syncId AND t.uzer.id = :userId ORDER BY t.startTime DESC")
    List<Task> findByProjectIdAndSyncId(
        @Param("projectId") Integer projectId,
        @Param("syncId") Integer syncId,
        @Param("userId") String userId
    );

    @Query("SELECT SUM(t.durationHr) FROM Task t WHERE t.status = 'COMPLETED' AND t.project.id = :projectId AND t.syncId = :syncId AND t.uzer.id = :userId")
    Double getTotalHoursSpent(
        @Param("projectId") Integer projectId,
        @Param("syncId") Integer syncId,
        @Param("userId") String userId
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.project.id = :projectId")
    void deleteAllByProjectId(@Param("projectId") Integer projectId);
}
