package com.odaat.odaat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.odaat.odaat.model.Uzer;

@Repository
public interface UzerRepository extends JpaRepository<Uzer, Integer> {
}