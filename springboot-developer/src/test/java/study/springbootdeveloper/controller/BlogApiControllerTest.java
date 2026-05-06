package study.springbootdeveloper.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import study.springbootdeveloper.domain.Article;
import study.springbootdeveloper.dto.AddArticleRequest;
import study.springbootdeveloper.dto.UpdateArticleRequest;
import study.springbootdeveloper.repository.BlogRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();
    }

    @Test
    public void addArticles() throws Exception {
        //given
        final String url = "/api/articles";
        AddArticleRequest addArticleRequest = new AddArticleRequest("titleTest", "contentTest");

        final String requestBody = objectMapper.writeValueAsString(addArticleRequest);

        //when
        ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

        //then
        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo("titleTest");
        assertThat(articles.get(0).getContent()).isEqualTo("contentTest");
    }

    @Test
    public void findAllArticles() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        ResultActions actions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));

    }

    @Test
    public void findArticles() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article article = blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        ResultActions actions = mockMvc.perform(get(url, article.getId()));

        //then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    public void deleteArticles() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article article = blogRepository.save(Article.builder().title(title).content(content).build());

        //when
        ResultActions actions = mockMvc.perform(delete(url, article.getId())).andExpect(status().isOk());

        //then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @Test
    public void updateArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article article = blogRepository.save(Article.builder().title(title).content(content).build());
        final String newTitle = "title4";
        final String newContent = "content4";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        //when
        ResultActions actions = mockMvc.perform(put(url, article.getId()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        actions.andExpect(status().isOk());

        Article updatedArticle = blogRepository.findById(article.getId()).get();

        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.getContent()).isEqualTo(newContent);

    }
}