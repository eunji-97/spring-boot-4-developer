package study.springbootdeveloper.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import study.springbootdeveloper.config.JwtTokenProvider;
import study.springbootdeveloper.domain.User;
import study.springbootdeveloper.service.RefreshTokenService;
import study.springbootdeveloper.service.UserService;
import study.springbootdeveloper.util.CookieUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 14;

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Value("${app.frontend-redirect-uri:http://localhost:5173/oauth/callback}")
    private String frontendRedirectUri;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        User user = userService.findByEmail(email);
        String accessToken = jwtTokenProvider.createToken(user.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        refreshTokenService.saveOrUpdate(user.getId(), refreshToken);
        CookieUtil.addCookie(response, "refresh_token", refreshToken, REFRESH_TOKEN_COOKIE_MAX_AGE);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendRedirectUri)
                .queryParam("accessToken", accessToken)
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
