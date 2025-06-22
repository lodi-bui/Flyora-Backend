package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.Faq;
import org.example.flyora_backend.model.NewsArticle;
import org.example.flyora_backend.model.Policy;
import org.example.flyora_backend.service.InfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Thông tin hệ thống", description = "Tin tức, FAQ, Chính sách")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/api/v1/news")
    @Operation(summary = "Lấy danh sách tin tức đã được đăng")
    public ResponseEntity<List<NewsArticle>> getNews() {
        return ResponseEntity.ok(infoService.getPublishedNews());
    }

    @GetMapping("/api/v1/faqs")
    @Operation(summary = "Lấy danh sách câu hỏi thường gặp")
    public ResponseEntity<List<Faq>> getFaqs() {
        return ResponseEntity.ok(infoService.getFaqs());
    }

    @GetMapping("/policies")
    @Operation(summary = "Lấy danh sách chính sách hiện hành")
    public ResponseEntity<List<Policy>> getPolicies() {
        return ResponseEntity.ok(infoService.getPolicies());
    }
}