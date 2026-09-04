package fr.diginamic.repository;

import fr.diginamic.entities.Departement;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Fournit les opérations d'accès aux données pour l'entité Departement.
 */
public interface DepartementRepository extends JpaRepository<Departement, Integer> {

  /**
   * Recherche un département par son code.
   *
   * @param code code du département recherché
   * @return le département correspondant, s'il existe
   */
  Optional<Departement> findByCode(String code);
}