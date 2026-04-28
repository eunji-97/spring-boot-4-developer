package study.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.springbootdeveloper.dto.CreateAccessTokenRequest;
import study.springbootdeveloper.dto.CreateAccessTokenResponse;
import study.springbootdeveloper.service.TokenService;

@RestController
@RequiredArgsConstructor
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request) {
        try {
            String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CreateAccessTokenResponse(newAccessToken));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
