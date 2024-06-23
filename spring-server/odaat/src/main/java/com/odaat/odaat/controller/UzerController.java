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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.dto.request.UzerNameUpdateRequest;
import com.odaat.odaat.dto.response.UzerResponse;
import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.service.UzerService;

/*
    #   UNDER DEVELOPMENT.
    #   USER PROFILES ARE NOT AVAILABLE YET IN v1.0.
   
   Controller for the "Uzer" entity.
   1. Endpoints
   - Update name, Delete.
   
   2. DTO and Entity conversion
   - Entity -> DTO
 */
@RestController
@RequestMapping("/api/uzer")
public class UzerController {

    @Autowired
    private UzerService uzerService;

    /* 1. Endpoints */

    @PutMapping("/{id}") // Update name
    public ResponseEntity<?> updateUzerName(@PathVariable String id, @Valid @RequestBody UzerNameUpdateRequest uzerRequest, BindingResult bindingResult) {
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

    @DeleteMapping("/{id}") // Delete
    public ResponseEntity<Void> deleteUzer(@PathVariable String id) {
        uzerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /* 2. DTO and Entity conversion */

    public UzerResponse convertToDto(Uzer uzer) { // Uzer to UzerResponse
        return new UzerResponse(uzer.getName(), uzer.getEmail(), uzer.getCreatedAt());
    }
}
