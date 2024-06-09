package com.odaat.odaat.controller;

import java.util.List;
import java.util.Optional;

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

import com.odaat.odaat.model.Uzer;
import com.odaat.odaat.service.UzerService;

@RestController
@RequestMapping("/api/uzers")
public class UzerController {

    @Autowired
    private UzerService uzerService;

    @GetMapping
    public List<Uzer> getAllUzers() {
        return uzerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Uzer> getUzerById(@PathVariable Integer id) {
        Optional<Uzer> uzer = uzerService.findById(id);
        return uzer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUzer(@Valid @RequestBody Uzer uzer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(uzerService.save(uzer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUzer(@PathVariable Integer id, @Valid @RequestBody Uzer uzer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Optional<Uzer> existingUzer = uzerService.findById(id);
        if (existingUzer.isPresent()) {
            uzer.setId(id);
            return ResponseEntity.ok(uzerService.save(uzer));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUzer(@PathVariable Integer id) {
        uzerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
