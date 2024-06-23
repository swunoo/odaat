package com.odaat.odaat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c WHERE c.uzer.id = :currUserId")
    List<Category> findByUzerId(@Param("currUserId") String currUserId);

    @Query("SELECT COUNT(c.id) FROM Category c WHERE c.uzer.id = :currUserId")
    Integer countByUserId(@Param("currUserId") String currUserId);
}
