package com.odaat.odaat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
