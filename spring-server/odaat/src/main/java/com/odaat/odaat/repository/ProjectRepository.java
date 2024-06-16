package com.odaat.odaat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odaat.odaat.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT p FROM Project p WHERE p.syncId = :id")
    Optional<Project> getBySyncId(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.isDeleted = TRUE, p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void softDelete(@Param("id") Integer id);
}
