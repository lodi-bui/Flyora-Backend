package org.example.flyora_backend.controller;

import org.example.flyora_backend.model.NewsArticle;
import org.example.flyora_backend.model.response.ResponseObject;
import org.example.flyora_backend.service.NewsArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
public class NewsArticleController {

    @Autowired
    private NewsArticleService newsArticleService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllNews() {
        return ResponseObject.APIResponse(
            200, "Get All News Success", HttpStatus.OK, newsArticleService.getAllArticles()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOneNews(@PathVariable Integer id) {
        return ResponseObject.APIResponse(
            200, "Get One News Success", HttpStatus.OK, newsArticleService.getOneArticle(id)
        );
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> addNews(@RequestBody NewsArticle news) {
        return ResponseObject.APIResponse(
            201, "Add News Success", HttpStatus.CREATED, newsArticleService.addArticle(news)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteNews(@PathVariable Integer id) {
        newsArticleService.deleteArticle(id);
        return ResponseObject.APIResponse(
            204, "Delete News Success", HttpStatus.NO_CONTENT, null
        );
    }
}
