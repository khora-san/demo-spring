package fr.diginamic.security;

import fr.diginamic.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurInitializer {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder encoder;

  public UtilisateurInitializer(UtilisateurRepository utilisateurRepository,
      PasswordEncoder encoder) {
    this.utilisateurRepository = utilisateurRepository;
    this.encoder = encoder;
  }

  @PostConstruct
  public void init() {
    if (utilisateurRepository.count() == 0) {
      Utilisateur user = new Utilisateur("user", encoder.encode("user1234"),
          new Role("ROLE_USER"));
      Utilisateur admin = new Utilisateur("admin", encoder.encode("admin1234"),
          new Role("ROLE_ADMIN"));

      utilisateurRepository.save(user);
      utilisateurRepository.save(admin);
    }
  }
}
