package com.odaat.odaat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
