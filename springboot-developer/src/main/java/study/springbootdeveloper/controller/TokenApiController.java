package study.springbootdeveloper.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springbootdeveloper.dto.CreateAccessTokenRequest;
import study.springbootdeveloper.dto.CreateAccessTokenResponse;
import study.springbootdeveloper.service.TokenService;
import study.springbootdeveloper.util.CookieUtil;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
            @RequestBody(required = false) CreateAccessTokenRequest request,
            HttpServletRequest httpServletRequest
    ) {
        try {
            String refreshToken = request != null ? request.getRefreshToken() : null;
            if (refreshToken == null || refreshToken.isBlank()) {
                Cookie cookie = CookieUtil.getCookie(httpServletRequest, "refresh_token");
                refreshToken = cookie != null ? cookie.getValue() : null;
            }

            String newAccessToken = tokenService.createNewAccessToken(refreshToken);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CreateAccessTokenResponse(newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
