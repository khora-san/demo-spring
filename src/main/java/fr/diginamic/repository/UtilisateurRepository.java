package fr.diginamic.repository;

import fr.diginamic.security.Utilisateur;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

  Optional<Utilisateur> findByUsername(String username);
}
