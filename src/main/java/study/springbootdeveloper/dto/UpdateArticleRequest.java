package study.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.springbootdeveloper.domain.Article;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
