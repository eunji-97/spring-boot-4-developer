package study.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.springbootdeveloper.domain.RefreshToken;
import study.springbootdeveloper.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository repository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return repository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("unexpected token"));
    }

    @Transactional
    public void saveOrUpdate(Long userId, String refreshToken) {
        repository.findByUserId(userId)
                .map(token -> token.update(refreshToken))
                .orElseGet(() -> repository.save(new RefreshToken(userId, refreshToken)));
    }
}
