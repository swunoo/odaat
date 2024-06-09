package com.odaat.odaat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Uzer;

@Service
public class SecurityService {
    public Uzer getCurrentUser() {
        Uzer mockUzer = new Uzer();
        mockUzer.setId(1);
        mockUzer.setName("Test User");
        return mockUzer;
    }
}
