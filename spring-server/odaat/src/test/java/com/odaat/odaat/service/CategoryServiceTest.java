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

import com.odaat.odaat.model.Category;
import com.odaat.odaat.repository.CategoryRepository;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Category category = new Category();
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<Category> categories = categoryService.findAll();
        assertEquals(1, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Category category = new Category();
        category.setId(1);
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.findById(1);
        assertTrue(foundCategory.isPresent());
        assertEquals(1, foundCategory.get().getId());
        verify(categoryRepository, times(1)).findById(anyInt());
    }

    @Test
    void testSave() {
        Category category = new Category();
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category savedCategory = categoryService.save(category);
        assertNotNull(savedCategory);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(categoryRepository).deleteById(anyInt());

        categoryService.deleteById(1);
        verify(categoryRepository, times(1)).deleteById(anyInt());
    }
}
