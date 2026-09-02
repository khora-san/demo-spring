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
    TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v", Ville.class);
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
    TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.nom LIKE :prefixe",
            Ville.class)
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
    TypedQuery<Ville> query = em.createQuery("SELECT v FROM Ville v WHERE v.population > :min",
            Ville.class)
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
            "SELECT v FROM Ville v WHERE v.population BETWEEN :min AND :max", Ville.class)
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
   * @param villeModifiee les nouvelles données à appliquer (nom, population)
   */
  public void merge(int idVille, Ville villeModifiee) {
    Ville villeExistante = em.find(Ville.class, idVille);
    villeExistante.setNom(villeModifiee.getNom());
    villeExistante.setPopulation(villeModifiee.getPopulation());
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