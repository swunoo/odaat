package com.odaat.odaat.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Project;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.model.enums.ProjectStatus;
import com.odaat.odaat.repository.CategoryRepository;

/*
    Service layer for the "Category" entity.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll(String userId) {
        return categoryRepository.findByUzerId(userId);
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Integer countCategories(String userId){
        return categoryRepository.countByUserId(userId);
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return categoryRepository.existsById(id);
    }

    public Category saveDefault(Uzer uzer) {
        Category category = new Category();
        category.setUzer(uzer);
        category.setName("Default");
        return categoryRepository.save(category);
    }

    // Categories are not yet supported by the client, so a default is used.
    public Category getDefaultCategory(String currUserId) {
        List<Category> categories = categoryRepository.findByUzerId(currUserId);

        if(categories == null || categories.isEmpty()){
            throw new NoSuchElementException("No category found");
        }

        return categories.get(0);
    }
}
