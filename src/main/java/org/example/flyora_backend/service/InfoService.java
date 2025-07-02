package org.example.flyora_backend.service;

import java.time.Instant;
import java.util.List;

import org.example.flyora_backend.DTOs.CreateNewsDTO;
import org.example.flyora_backend.model.Admin;
import org.example.flyora_backend.model.Faq;
import org.example.flyora_backend.model.NewsArticle;
import org.example.flyora_backend.model.Policy;
import org.example.flyora_backend.repository.AdminRepository;
import org.example.flyora_backend.repository.FaqRepository;
import org.example.flyora_backend.repository.NewsArticleRepository;
import org.example.flyora_backend.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final FaqRepository faqRepository;
    private final PolicyRepository policyRepository;
    private final NewsArticleRepository newsArticleRepository;
    private final AdminRepository adminRepository;

    public List<Faq> getFaqs() {
        return faqRepository.findAll();
    }

    public List<Policy> getPolicies() {
        return policyRepository.findAll();
    }

    public List<NewsArticle> getPublishedNews() {
        return newsArticleRepository.findByIsPublishedTrueOrderByPublishedAtDesc();
    }

    public NewsArticle createNewsArticle(CreateNewsDTO dto, Integer requesterId) {
        Admin admin = adminRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy admin"));

        NewsArticle article = new NewsArticle();
        article.setId(newsArticleRepository.findAll().stream()
            .mapToInt(n -> n.getId() != null ? n.getId() : 0).max().orElse(0) + 1);
        article.setTitle(dto.getTitle());
        article.setImageUrl(dto.getUrl()); // có thể crawl thumbnail sau
        article.setContent("Đang cập nhật nội dung..."); // có thể crawl sau
        article.setPublishedAt(Instant.now());
        article.setIsPublished(true);
        article.setCreatedBy(admin);

        return newsArticleRepository.save(article);
    }

}