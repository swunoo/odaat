package com.odaat.odaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.repository.UzerRepository;

@Service
public class UzerService {

    @Autowired
    private UzerRepository uzerRepository;

    public List<Uzer> findAll() {
        return uzerRepository.findAll();
    }

    public Optional<Uzer> findById(Integer id) {
        return uzerRepository.findById(id);
    }

    public Uzer save(Uzer uzer) {
        return uzerRepository.save(uzer);
    }

    public void deleteById(Integer id) {
        uzerRepository.softDelete(id);
    }
}
