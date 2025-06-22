package org.example.flyora_backend.service;

import java.util.List;

import org.example.flyora_backend.model.Faq;
import org.example.flyora_backend.model.NewsArticle;
import org.example.flyora_backend.model.Policy;
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

    public List<Faq> getFaqs() {
        return faqRepository.findAll();
    }

    public List<Policy> getPolicies() {
        return policyRepository.findAll();
    }

    public List<NewsArticle> getPublishedNews() {
        return newsArticleRepository.findByIsPublishedTrueOrderByPublishedAtDesc();
    }
}