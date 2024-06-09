package com.odaat.odaat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
