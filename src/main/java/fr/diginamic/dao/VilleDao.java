package fr.diginamic.dao;

import fr.diginamic.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * DAO d'accès aux données pour l'entité {@link Ville}.
 * <p>
 * Seule cette classe est autorisée à utiliser l'{@link EntityManager} pour dialoguer avec la base
 * de données ; la couche service ne doit jamais y accéder directement.
 */
@Repository
public class VilleDao {

  @PersistenceContext
  private EntityManager em;

  /**
   * Récupère l'ensemble des villes présentes en base.
   *
   * @return la liste de toutes les villes
   */
  public List<Ville> findVilles() {
    TypedQuery<Ville> query = em.createQuery(
        "SELECT v FROM Ville v JOIN FETCH v.departement", Ville.class);
    return query.getResultList();
  }

  /**
   * Récupère la ville correspondant à l'identifiant donné.
   *
   * @param id l'identifiant de la ville recherchée
   * @return la ville correspondante, ou {@code null} si aucune ville ne correspond
   */
  public Ville findById(Integer id) {
    return em.find(Ville.class, id);
  }

  /**
   * Récupère les villes dont le nom commence par le préfixe donné.
   *
   * @param prefixe le préfixe recherché dans le nom des villes
   * @return la liste des villes correspondantes (éventuellement vide)
   */
  public List<Ville> findVillesByNomPrefixe(String prefixe) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.nom LIKE :prefixe", Ville.class)
        .setParameter("prefixe", prefixe + "%");
    return query.getResultList();
  }

  /**
   * Récupère les villes dont la population est strictement supérieure au minimum donné.
   *
   * @param min le seuil minimum de population (exclu)
   * @return la liste des villes correspondantes (éventuellement vide)
   */
  public List<Ville> findVillesByPopulationSuperieure(int min) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.population > :min", Ville.class)
        .setParameter("min", min);
    return query.getResultList();
  }

  /**
   * Récupère les villes dont la population est comprise entre les bornes données.
   *
   * @param min la population minimum
   * @param max la population maximum
   * @return la liste des villes correspondantes (éventuellement vide)
   */
  public List<Ville> findVillesByPopulationEntre(int min, int max) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v JOIN FETCH v.departement WHERE v.population BETWEEN :min AND :max",
            Ville.class)
        .setParameter("min", min)
        .setParameter("max", max);
    return query.getResultList();
  }

  /**
   * Récupère les n villes les plus peuplées d'un département donné.
   *
   * @param code code du département concerné
   * @param n    nombre de villes à retourner
   * @return la liste des n villes les plus peuplées du département, triées par population
   * décroissante
   */
  public List<Ville> findTopVillesByDepartementCode(String code, int n) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v JOIN FETCH v.departement d WHERE d.code = :code "
                + "ORDER BY v.population DESC",
            Ville.class)
        .setParameter("code", code);
    return query.setMaxResults(n).getResultList();
  }

  /**
   * Récupère les villes d'un département donné dont la population est comprise entre deux bornes.
   *
   * @param code code du département concerné
   * @param min  population minimale (incluse)
   * @param max  population maximale (incluse)
   * @return la liste des villes du département dont la population est comprise entre min et max
   */
  public List<Ville> findVillesByPopulationEntreAndDepartementCode(String code, int min, int max) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v JOIN FETCH v.departement d WHERE d.code = :code "
                + "AND v.population BETWEEN :min AND :max",
            Ville.class)
        .setParameter("code", code)
        .setParameter("min", min)
        .setParameter("max", max);
    return query.getResultList();
  }

  /**
   * Vérifie si une ville portant exactement ce nom existe déjà en base.
   *
   * @param nom le nom à vérifier
   * @return {@code true} si une ville porte déjà ce nom, {@code false} sinon
   */
  public boolean existsVilleByNom(String nom) {
    TypedQuery<Ville> query = em.createQuery(
            "SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class)
        .setParameter("nom", nom);
    return !query.getResultList().isEmpty();
  }

  /**
   * Persiste une nouvelle ville en base.
   *
   * @param ville la ville à créer (son identifiant doit être {@code null}, il sera généré par la
   *              base)
   */
  public void persist(Ville ville) {
    em.persist(ville);
  }

  /**
   * Met à jour une ville existante avec les nouvelles données fournies. L'identifiant de la ville à
   * modifier est celui passé en paramètre, l'éventuel identifiant porté par {@code villeModifiee}
   * est ignoré.
   *
   * @param idVille       l'identifiant de la ville à modifier
   * @param villeModifiee les nouvelles données à appliquer (nom, population, département)
   */
  public void merge(int idVille, Ville villeModifiee) {
    Ville villeExistante = em.find(Ville.class, idVille);
    villeExistante.setNom(villeModifiee.getNom());
    villeExistante.setPopulation(villeModifiee.getPopulation());
    villeExistante.setDepartement(villeModifiee.getDepartement());
  }

  /**
   * Supprime une ville de la base.
   *
   * @param ville la ville à supprimer (doit être une entité managée)
   */
  public void remove(Ville ville) {
    em.remove(ville);
  }

}