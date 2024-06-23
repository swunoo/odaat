package com.odaat.odaat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT p FROM Project p WHERE p.uzer.id = :currUserId")
    List<Project> findByUzerId(@Param("currUserId") String currUserId);

    @Query("SELECT COUNT(p.id) FROM Project p WHERE p.uzer.id = :currUserId")
    Integer countByUserId(@Param("currUserId") String currUserId);

    @Query("SELECT p FROM Project p WHERE p.syncId = :syncId AND p.uzer.id = :currUserId")
    Optional<Project> getBySyncId(@Param("syncId") Integer syncId, @Param("currUserId") String currUserId);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.isDeleted = TRUE, p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void softDelete(@Param("id") Integer id);

}
