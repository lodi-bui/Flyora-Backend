package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.model.Faq;
import org.example.flyora_backend.model.NewsArticle;
import org.example.flyora_backend.model.Policy;
import org.example.flyora_backend.service.InfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "News & Blogs", description = "API lấy tin tức, câu hỏi thường gặp, chính sách hệ thống")
public class InfoController {

    private final InfoService infoService;

    @GetMapping("/news")
    @Operation(
        summary = "Lấy danh sách tin tức đã được đăng",
        description = """
            ✅ Trả về danh sách các bài viết (`NewsArticle`) đã được đánh dấu là `isPublished = true`.

            Mỗi bài viết gồm:
            - id (Integer)
            - title (String)
            - content (Text)
            - imageUrl (String)
            - publishedAt (Datetime)
        """
    )
    public ResponseEntity<List<NewsArticle>> getNews() {
        return ResponseEntity.ok(infoService.getPublishedNews());
    }

    @GetMapping("/faqs")
    @Operation(
        summary = "Lấy danh sách câu hỏi thường gặp (FAQ)",
        description = """
            ✅ Trả về danh sách câu hỏi (`Faq`) được sử dụng phổ biến trong hệ thống.

            Mỗi mục FAQ gồm:
            - id (Integer)
            - question (String)
            - answer (Text)
        """
    )
    public ResponseEntity<List<Faq>> getFaqs() {
        return ResponseEntity.ok(infoService.getFaqs());
    }

    @GetMapping("/policies")
    @Operation(
        summary = "Lấy danh sách chính sách hệ thống",
        description = """
            ✅ Trả về toàn bộ chính sách đang được áp dụng (`Policy`).

            Mỗi chính sách gồm:
            - id (Integer)
            - content (Text)
        """
    )
    public ResponseEntity<List<Policy>> getPolicies() {
        return ResponseEntity.ok(infoService.getPolicies());
    }
}
