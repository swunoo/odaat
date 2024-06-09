package com.odaat.odaat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.repository.UzerRepository;

class UzerServiceTest {

    @Mock
    private UzerRepository uzerRepository;

    @InjectMocks
    private UzerService uzerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Uzer uzer = new Uzer();
        when(uzerRepository.findAll()).thenReturn(Collections.singletonList(uzer));

        List<Uzer> uzers = uzerService.findAll();
        assertEquals(1, uzers.size());
        verify(uzerRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Uzer uzer = new Uzer();
        uzer.setId(1);
        when(uzerRepository.findById(anyInt())).thenReturn(Optional.of(uzer));

        Optional<Uzer> foundUzer = uzerService.findById(1);
        assertTrue(foundUzer.isPresent());
        assertEquals(1, foundUzer.get().getId());
        verify(uzerRepository, times(1)).findById(anyInt());
    }

    @Test
    void testSave() {
        Uzer uzer = new Uzer();
        when(uzerRepository.save(any(Uzer.class))).thenReturn(uzer);

        Uzer savedUzer = uzerService.save(uzer);
        assertNotNull(savedUzer);
        verify(uzerRepository, times(1)).save(any(Uzer.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(uzerRepository).softDelete(anyInt());

        uzerService.deleteById(1);
        verify(uzerRepository, times(1)).softDelete(anyInt());
    }
}
