package fr.diginamic.controlers;

import fr.diginamic.dto.LoginRequest;
import fr.diginamic.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginControleur {

  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;

  public LoginControleur(AuthenticationManager authManager, JwtUtil jwtUtil) {
    this.authManager = authManager;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public String login(@RequestBody LoginRequest req) {
// Déclenche la vérification du mot de passe avec UserDetailsService
    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.username(), req.password()));
// Retourne un JWT
    return jwtUtil.generateToken(req.username());
  }
}