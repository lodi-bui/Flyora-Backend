package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.BirdType;
import org.example.flyora_backend.repository.BirdTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/birdtypes")
@Tag(name = "Bird Type API", description = "Quản lý loại chim")
public class BirdTypeController {

    @Autowired
    private BirdTypeRepository birdTypeRepository;

    @GetMapping
    @Operation(summary = "Lấy tất cả loại chim", description = "Trả về danh sách các loại chim hiện có trong hệ thống")
    public ResponseEntity<List<BirdType>> getAllBirdTypes() {
        return ResponseEntity.ok(birdTypeRepository.findAll());
    }
}
