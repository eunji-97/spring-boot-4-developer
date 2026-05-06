package study.springbootdeveloper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springbootdeveloper.config.JwtTokenProvider;
import study.springbootdeveloper.domain.User;
import study.springbootdeveloper.dto.AddUserRequest;
import study.springbootdeveloper.dto.LoginRequest;
import study.springbootdeveloper.dto.LoginResponse;
import study.springbootdeveloper.service.RefreshTokenService;
import study.springbootdeveloper.service.UserService;
import study.springbootdeveloper.util.CookieUtil;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private static final int REFRESH_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 14;

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/user")
    public ResponseEntity<User> signup(@RequestBody AddUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request));
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            String accessToken = jwtTokenProvider.createToken(user.getEmail());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            refreshTokenService.saveOrUpdate(user.getId(), refreshToken);
            CookieUtil.addCookie(response, "refresh_token", refreshToken, REFRESH_TOKEN_COOKIE_MAX_AGE);

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Object principal = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : null;

        if (principal instanceof User user) {
            refreshTokenService.deleteByUserId(userService.findByEmail(user.getEmail()).getId());
        }

        CookieUtil.deleteCookie(request, response, "refresh_token");
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
