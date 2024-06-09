package com.odaat.odaat.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.dao.request.CategoryRequest;
import com.odaat.odaat.dao.response.CategoryResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.service.CategoryService;
import com.odaat.odaat.service.SecurityService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SecurityService securityService;

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll().stream()
                .map(this::convertToDao)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);

        if (!category.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDao(category.get()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Category category = convertToEntity(categoryRequest);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(convertToDao(savedCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        if (!categoryService.existsById(id)) {
            return ResponseEntity.badRequest().build();
            
        } else {
            Category category = convertToEntity(categoryRequest);
            category.setId(id);
            Category savedCategory = categoryService.save(category);
            return ResponseEntity.ok(convertToDao(savedCategory));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DAO CONVERTERS
    private CategoryResponse convertToDao(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    private Category convertToEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setUzer(securityService.getCurrentUser());
        category.setName(categoryRequest.getName());
        return category;
    }

}
