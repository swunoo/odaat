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

import com.odaat.odaat.model.Category;
import com.odaat.odaat.service.CategoryService;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.singletonList(new Category()));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).findAll();
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = new Category();
        category.setId(1);
        when(categoryService.findById(anyInt())).thenReturn(Optional.of(category));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(categoryService, times(1)).findById(anyInt());
    }

    @Test
    void testCreateCategory() throws Exception {
        Category category = new Category();
        when(categoryService.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Test Category\", \"uzerId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist());

        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = new Category();
        category.setId(1);
        when(categoryService.findById(anyInt())).thenReturn(Optional.of(category));
        when(categoryService.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Updated Category\", \"uzerId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(categoryService, times(1)).findById(anyInt());
        verify(categoryService, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteById(anyInt());

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(anyInt());
    }
}
