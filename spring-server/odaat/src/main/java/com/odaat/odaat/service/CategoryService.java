package com.odaat.odaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Category;
import com.odaat.odaat.repository.CategoryRepository;

/*
    Service layer for the "Category" entity.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthService authService;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return categoryRepository.existsById(id);
    }

    // Categories are not yet supported by the client, so a default is used.
    public Category getDefaultCategory(){
        String currUserId = authService.getCurrentUserId();
        Optional<Category> category = categoryRepository.findByUzerId(currUserId);
        if(category.isPresent()) return category.get();

        Category defaultCategory = new Category();
        defaultCategory.setName("Default");
        defaultCategory.setUzer(authService.getCurrentUser());
        return categoryRepository.save(defaultCategory);
    }
}
