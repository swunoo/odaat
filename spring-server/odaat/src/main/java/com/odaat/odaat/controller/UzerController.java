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
import com.odaat.odaat.dto.request.UzerNameUpdateRequest;
import com.odaat.odaat.dto.response.CategoryResponse;
import com.odaat.odaat.dto.response.UzerResponse;
import com.odaat.odaat.model.Category;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.UzerService;

@RestController
@RequestMapping("/api/uzer")
public class UzerController {

    @Autowired
    private UzerService uzerService;

    @GetMapping
    public List<UzerResponse> getAllUzers() {
        return uzerService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UzerResponse> getUzerById(@PathVariable Integer id) {
        Optional<Uzer> uzer = uzerService.findById(id);
        if (!uzer.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(convertToDto(uzer.get()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUzerName(@PathVariable Integer id, @Valid @RequestBody UzerNameUpdateRequest uzerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Optional<Uzer> existingUzer = uzerService.findById(id);

        if (!existingUzer.isPresent()) {
            return ResponseEntity.badRequest().build();

            } else {
                Uzer uzer = existingUzer.get();
                uzer.setName(uzerRequest.getName());
                uzer = uzerService.save(uzer);
                return ResponseEntity.ok(convertToDto(uzer));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUzer(@PathVariable Integer id) {
        uzerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DTO and Entity Conversion
    public UzerResponse convertToDto(Uzer uzer) {
        return new UzerResponse(uzer.getName(), uzer.getEmail(), uzer.getCreatedAt());
    }
}
