package study.springbootdeveloper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springbootdeveloper.domain.Article;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
