package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.AddArticleRequest;
import study.springbootdeveloper.dto.ArticleResponse;
import study.springbootdeveloper.dto.UpdateArticleRequest;
import study.springbootdeveloper.service.BlogService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest addArticleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(blogService.save(addArticleRequest));
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(blogService.findAll().stream().map(ArticleResponse::new).toList());
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticles(@PathVariable Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticles(@PathVariable Long id) {
        blogService.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(blogService.update(id, request));
    }
}
