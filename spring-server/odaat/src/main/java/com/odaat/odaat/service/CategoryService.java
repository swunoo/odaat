package com.odaat.odaat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Category;
import com.odaat.odaat.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

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

    // TODO: Save this DEFAULT in memory, and avoid calling database each time.
    public Category getDefaultCategory(){
        return categoryRepository.findById(1).orElse(new Category());
    }
}
