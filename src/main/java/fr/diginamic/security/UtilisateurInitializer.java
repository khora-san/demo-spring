package fr.diginamic.security;

import fr.diginamic.repository.UtilisateurRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialise deux utilisateurs de test en base de données au démarrage de l'application : un
 * utilisateur avec le profil ROLE_USER et un administrateur avec le profil ROLE_ADMIN. Les mots de
 * passe sont hashés via {@link PasswordEncoder} avant sauvegarde.
 * <p>
 * L'initialisation ne s'exécute qu'une seule fois : si la table des utilisateurs contient déjà des
 * données, aucune création n'est effectuée (évite les doublons aux redémarrages suivants).
 */
@Component
public class UtilisateurInitializer {

  private final UtilisateurRepository utilisateurRepository;
  private final PasswordEncoder encoder;

  public UtilisateurInitializer(UtilisateurRepository utilisateurRepository,
      PasswordEncoder encoder) {
    this.utilisateurRepository = utilisateurRepository;
    this.encoder = encoder;
  }

  /**
   * Crée les utilisateurs "user" (ROLE_USER) et "admin" (ROLE_ADMIN) si la base est vide. Exécutée
   * automatiquement après l'injection des dépendances, grâce à {@link PostConstruct}.
   */
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