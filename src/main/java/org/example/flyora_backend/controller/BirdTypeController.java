package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.BirdType;
import org.example.flyora_backend.repository.BirdTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/birdtypes")
public class BirdTypeController {

    @Autowired
    private BirdTypeRepository birdTypeRepository;

    @GetMapping
    public ResponseEntity<List<BirdType>> getAllBirdTypes() {
        return ResponseEntity.ok(birdTypeRepository.findAll());
    }
}
