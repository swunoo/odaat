package com.odaat.odaat.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odaat.odaat.dto.request.UzerNameUpdateRequest;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.UzerService;
import com.odaat.odaat.utils.MockUtil;

@ExtendWith(MockitoExtension.class)
class UzerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UzerService uzerService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UzerController uzerController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(uzerController).build();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testUpdateUzername() throws Exception {
        Uzer uzer = MockUtil.mockInstance(Uzer.class);
        UzerNameUpdateRequest updateRequest = new UzerNameUpdateRequest();
        updateRequest.setName("Updated Name");
        when(uzerService.findById(any())).thenReturn(Optional.of(uzer));
        when(uzerService.save(any(Uzer.class))).thenReturn(uzer);

        mockMvc.perform(put("/api/uzer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));

        verify(uzerService, times(1)).save(any(Uzer.class));
    }

    @Test
    void testDeleteUzer() throws Exception {
        doNothing().when(uzerService).deleteById(any());

        mockMvc.perform(delete("/api/uzer/1"))
                .andExpect(status().isNoContent());

        verify(uzerService, times(1)).deleteById(any());
    }
}