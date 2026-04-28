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

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/api/user")
    public ResponseEntity<User> signup(@RequestBody AddUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request));
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getEmail(), request.getPassword());
            String accessToken = jwtTokenProvider.createToken(user.getEmail());
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
            refreshTokenService.saveOrUpdate(user.getId(), refreshToken);

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
