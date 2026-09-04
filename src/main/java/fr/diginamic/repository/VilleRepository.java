package fr.diginamic.repository;

import fr.diginamic.entities.Ville;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA pour l'entité Ville.
 * <p>
 * À la différence de VilleDao (implémentation manuelle via EntityManager), une interface Repository
 * ne nécessite aucune implémentation : au démarrage de l'application, Spring Data génère
 * automatiquement une classe concrète qui traduit chaque méthode déclarée ci-dessous en requête
 * JPQL/SQL, soit par analyse du nom de la méthode ("requête dérivée"), soit à partir d'une requête
 * explicite via {@code @Query} lorsque le nom seul ne suffit pas à exprimer le besoin.
 * <p>
 * {@code JpaRepository<Ville, Integer>} fournit déjà les opérations CRUD de base ({@code save},
 * {@code findById}, {@code findAll}, {@code deleteById}, {@code existsById}, etc.), ainsi que des
 * variantes paginées et triées ({@code findAll(Pageable)}) héritées de
 * {@code PagingAndSortingRepository}. Seules les méthodes de recherche propres au métier sont
 * déclarées ici.
 */
public interface VilleRepository extends JpaRepository<Ville, Integer> {

  /**
   * Recherche les villes dont le nom commence par la chaîne donnée.
   *
   * @param prefixe préfixe recherché dans le nom de la ville
   * @return la liste des villes correspondantes
   */
  List<Ville> findByNomStartingWith(String prefixe);

  /**
   * Recherche les villes dont la population est strictement supérieure à une valeur minimale,
   * triées par population décroissante.
   *
   * @param min population minimale (exclusive)
   * @return la liste des villes correspondantes, triées par population décroissante
   */
  List<Ville> findByPopulationGreaterThanOrderByPopulationDesc(int min);

  /**
   * Recherche les villes dont la population est strictement comprise entre deux bornes, triées par
   * population décroissante.
   *
   * @param min population minimale (exclusive)
   * @param max population maximale (exclusive)
   * @return la liste des villes correspondantes, triées par population décroissante
   */
  List<Ville> findByPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(int min,
      int max);

  /**
   * Recherche les villes d'un département donné dont la population est strictement supérieure à une
   * valeur minimale, triées par population décroissante.
   *
   * @param code code du département concerné
   * @param min  population minimale (exclusive)
   * @return la liste des villes correspondantes, triées par population décroissante
   */
  List<Ville> findByDepartementCodeAndPopulationGreaterThanOrderByPopulationDesc(String code,
      int min);

  /**
   * Recherche les villes d'un département donné dont la population est strictement comprise entre
   * deux bornes, triées par population décroissante.
   *
   * @param code code du département concerné
   * @param min  population minimale (exclusive)
   * @param max  population maximale (exclusive)
   * @return la liste des villes correspondantes, triées par population décroissante
   */
  List<Ville> findByDepartementCodeAndPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(
      String code, int min, int max);

  /**
   * Recherche les villes les plus peuplées d'un département donné, triées par population
   * décroissante. Le nombre de résultats retournés est déterminé par le paramètre {@code pageable}
   * (voir {@link org.springframework.data.domain.PageRequest#of(int, int)}).
   *
   * @param code     code du département concerné
   * @param pageable paramètres de pagination définissant notamment le nombre de résultats
   * @return la liste des villes correspondantes, triées par population décroissante
   */
  List<Ville> findByDepartementCodeOrderByPopulationDesc(String code, Pageable pageable);

  /**
   *
   * @param nom
   * @return
   */
  boolean existsByNom(String nom);
}