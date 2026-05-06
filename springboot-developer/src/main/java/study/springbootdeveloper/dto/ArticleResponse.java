package study.springbootdeveloper.dto;

import lombok.Getter;
import study.springbootdeveloper.domain.Article;

import java.time.LocalDateTime;

@Getter
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String content;
    private LocalDateTime createdAt;

    public ArticleResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
    }

}
