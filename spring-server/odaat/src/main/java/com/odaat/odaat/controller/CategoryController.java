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

import com.odaat.odaat.dto.request.CategoryRequest;
import com.odaat.odaat.dto.response.CategoryResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.service.CategoryService;
import com.odaat.odaat.service.AuthService;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthService authService;

    @GetMapping("/get")
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        Optional<Category> category = categoryService.findById(id);

        if (!category.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDto(category.get()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Category category = convertToEntity(categoryRequest);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(convertToDto(savedCategory));
    }

    @PutMapping("/update/{id}")
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
            return ResponseEntity.ok(convertToDto(savedCategory));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO and Entity Conversion
    public CategoryResponse convertToDto(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    public Category convertToEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setUzer(authService.getCurrentUser());
        category.setName(categoryRequest.getName());
        return category;
    }

}
