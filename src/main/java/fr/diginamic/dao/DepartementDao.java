package fr.diginamic.dao;

import fr.diginamic.entities.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Dao permettant l'accès aux données de l'entité Departement. Seule cette classe est autorisée à
 * manipuler l'EntityManager pour les départements.
 */
@Repository
public class DepartementDao {

  @PersistenceContext
  private EntityManager em;

  /**
   * Récupère l'ensemble des départements.
   *
   * @return la liste de tous les départements
   */
  public List<Departement> findDepartements() {
    TypedQuery<Departement> query = em.createQuery("SELECT d FROM Departement d",
        Departement.class);
    return query.getResultList();
  }

  /**
   * Recherche un département à partir de son identifiant.
   *
   * @param id identifiant du département recherché
   * @return le département correspondant, ou null si aucun département ne possède cet identifiant
   */
  public Departement findById(Integer id) {
    return em.find(Departement.class, id);
  }

  /**
   * Recherche un département à partir de son code.
   *
   * @param code code du département recherché
   * @return le département correspondant, ou null si aucun département ne possède ce code
   */
  public Departement findByCode(String code) {
    TypedQuery<Departement> query = em.createQuery(
            "SELECT d FROM Departement d WHERE d.code = :code", Departement.class)
        .setParameter("code", code);
    List<Departement> resultats = query.getResultList();
    return resultats.isEmpty() ? null : resultats.get(0);
  }

  /**
   * Persiste un nouveau département en base de données.
   *
   * @param departement département à persister
   */
  public void persist(Departement departement) {
    em.persist(departement);
  }

  /**
   * Met à jour un département existant à partir de son identifiant. Le département géré par le
   * contexte de persistance est modifié, la mise à jour en base est effectuée par dirty-checking.
   *
   * @param idDepartement      identifiant du département à modifier
   * @param departementModifie département contenant les nouvelles valeurs
   * @return le département correspondant
   */
  public Departement merge(int idDepartement, Departement departementModifie) {
    Departement departementExistant = em.find(Departement.class, idDepartement);
    departementExistant.setCode(departementModifie.getCode());
    departementExistant.setNom(departementModifie.getNom());
    return departementExistant;
  }

  /**
   * Supprime un département de la base de données.
   *
   * @param departement département à supprimer
   */
  public void remove(Departement departement) {
    em.remove(departement);
  }
}