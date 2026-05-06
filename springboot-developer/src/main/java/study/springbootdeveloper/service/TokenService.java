package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.springbootdeveloper.config.JwtTokenProvider;
import study.springbootdeveloper.domain.User;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtTokenProvider provider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        if (!provider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken)
                .getUserId();
        User user = userService.findById(userId);

        return provider.createToken(user.getEmail());
    }
}
