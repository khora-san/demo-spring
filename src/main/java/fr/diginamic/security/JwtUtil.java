package fr.diginamic.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET = "secret-secret-secret-secret-secret-secret";
  private final SecretKey key =
      Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

  public String generateToken(String username) {
    return Jwts.builder().subject(username).issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    Claims claims = Jwts.parser().verifyWith(key)
        .build()
        .parseSignedClaims(token).getPayload();
    return claims.getSubject();
  }
}
