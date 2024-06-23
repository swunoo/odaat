package com.odaat.odaat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odaat.odaat.model.Uzer;

@Repository
public interface UzerRepository extends JpaRepository<Uzer, String> {
    @Modifying
    @Transactional
    @Query("UPDATE Uzer u SET u.isDeleted = TRUE, u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void softDelete(@Param("id") String id);
}