package study.springbootdeveloper.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.springbootdeveloper.config.JwtTokenProvider;
import study.springbootdeveloper.domain.User;
import study.springbootdeveloper.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @Test
    void createToken_and_getEmail() {
        // given
        String email = "test@test.com";

        User user = User.builder()
                .email(email)
                .password("1234")
                .build();

        repository.save(user);

        // when
        String token = tokenProvider.createToken(email);
        String extractedEmail = tokenProvider.getEmail(token);

        // then
        assertNotNull(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void token_should_not_be_expired_immediately() {
        String token = tokenProvider.createToken("test@test.com");
        String email = tokenProvider.getEmail(token);
        assertEquals("test@test.com", email);
    }

    @Test
    void invalid_token_should_throw_exception() {

        // given
        String invalidToken = "this.is.invalid.token";

        // when & then
        assertThrows(Exception.class, () -> {
            tokenProvider.getEmail(invalidToken);
        });

    }
}