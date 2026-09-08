package fr.diginamic.security;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemoireService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final Map<String, Utilisateur> utilisateurs;

  public MemoireService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    this.utilisateurs = new HashMap<>();
    utilisateurs.put("user",
        new Utilisateur("user", passwordEncoder.encode("user1234"), new Role("ROLE_USER")));
    utilisateurs.put("admin",
        new Utilisateur("admin", passwordEncoder.encode("admin1234"), new Role("ROLE_ADMIN")));

  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Utilisateur utilisateur = utilisateurs.get(username);
    if (utilisateur == null) {
      throw new UsernameNotFoundException("Vos paramètres d'authentification sont erronés");
    }
    return utilisateur;
  }
}
