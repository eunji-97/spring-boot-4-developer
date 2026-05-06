package study.springbootdeveloper.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import study.springbootdeveloper.domain.User;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRE_MILLIS = 1000L * 60 * 60;
    private static final long REFRESH_TOKEN_EXPIRE_MILLIS = 1000L * 60 * 60 * 24 * 14;

    private final Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret-key:my-very-secret-key-my-very-secret-key-123456}") String secretKey
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String email) {
        return createToken(email, ACCESS_TOKEN_EXPIRE_MILLIS);
    }

    public String createRefreshToken(String email) {
        return createToken(email, REFRESH_TOKEN_EXPIRE_MILLIS);
    }

    private String createToken(String email, long expirationMillis) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        User principal = new User(
                email,
                "",
                ""
        );

        return new UsernamePasswordAuthenticationToken(
                principal,
                "",
                principal.getAuthorities()
        );
    }
}
