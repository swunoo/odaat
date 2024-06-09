package com.odaat.odaat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.service.UzerService;

class UzerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UzerService uzerService;

    @InjectMocks
    private UzerController uzerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(uzerController).build();
    }

    @Test
    void testGetAllUzers() throws Exception {
        when(uzerService.findAll()).thenReturn(Collections.singletonList(new Uzer()));

        mockMvc.perform(get("/api/uzers"))
                .andExpect(status().isOk());

        verify(uzerService, times(1)).findAll();
    }

    @Test
    void testGetUzerById() throws Exception {
        Uzer uzer = new Uzer();
        uzer.setId(1);
        when(uzerService.findById(anyInt())).thenReturn(Optional.of(uzer));

        mockMvc.perform(get("/api/uzers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(uzerService, times(1)).findById(anyInt());
    }

    @Test
    void testCreateUzer() throws Exception {
        Uzer uzer = new Uzer();
        when(uzerService.save(any(Uzer.class))).thenReturn(uzer);

        mockMvc.perform(post("/api/uzers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Uzer\", \"email\": \"test@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(uzerService, times(1)).save(any(Uzer.class));
    }

    @Test
    void testUpdateUzer() throws Exception {
        Uzer uzer = new Uzer();
        uzer.setId(1);
        when(uzerService.findById(anyInt())).thenReturn(Optional.of(uzer));
        when(uzerService.save(any(Uzer.class))).thenReturn(uzer);

        mockMvc.perform(put("/api/uzers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Uzer\", \"email\": \"updated@example.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(uzerService, times(1)).findById(anyInt());
        verify(uzerService, times(1)).save(any(Uzer.class));
    }

    @Test
    void testDeleteUzer() throws Exception {
        doNothing().when(uzerService).deleteById(anyInt());

        mockMvc.perform(delete("/api/uzers/1"))
                .andExpect(status().isNoContent());

        verify(uzerService, times(1)).deleteById(anyInt());
    }
}