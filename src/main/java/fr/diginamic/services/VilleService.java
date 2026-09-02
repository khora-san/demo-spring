package fr.diginamic.services;

import fr.diginamic.dao.VilleDao;
import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier pour la gestion des villes.
 * <p>
 * Cette classe centralise les règles métier (unicité du nom, existence d'une ville) et orchestre
 * les appels à {@link VilleDao}, seule classe autorisée à dialoguer avec l'{@code EntityManager}.
 */
@Service
public class VilleService {

  private final VilleDao villeDao;

  /**
   * Construit le service en lui injectant sa dépendance vers le DAO.
   *
   * @param villeDao le DAO utilisé pour accéder aux données des villes
   */
  public VilleService(VilleDao villeDao) {
    this.villeDao = villeDao;
  }

  /**
   * Extrait toutes les villes présentes en base.
   *
   * @return la liste de toutes les villes
   */
  public List<Ville> extractVilles() {
    return villeDao.findVilles();
  }

  /**
   * Extrait la ville correspondant à l'identifiant donné.
   *
   * @param id l'identifiant de la ville recherchée
   * @return la ville correspondante
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  public Ville extractVille(int id) throws ExceptionFonctionnelle {
    Ville ville = villeDao.findById(id);
    if (ville == null) {
      throw new ExceptionFonctionnelle("Ville non trouvée");
    }
    return ville;
  }

  /**
   * Extrait les villes dont le nom commence par le préfixe donné.
   *
   * @param prefixe le préfixe recherché dans le nom des villes
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne correspond au préfixe
   */
  public List<Ville> extractVillesByNameStartWith(String prefixe) throws ExceptionFonctionnelle {
    List<Ville> villes = villeDao.findVillesByNomPrefixe(prefixe);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Extrait les villes dont la population est strictement supérieure au minimum donné.
   *
   * @param min le seuil minimum de population (exclu)
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne dépasse ce seuil
   */
  public List<Ville> extractVillesByPopulationSuperieure(int min) throws ExceptionFonctionnelle {
    List<Ville> villes = villeDao.findVillesByPopulationSuperieure(min);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Extrait les villes dont la population est comprise entre les bornes données.
   *
   * @param min la population minimum
   * @param max la population maximum
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet intervalle
   */
  public List<Ville> extractVillesByPopulationEntre(int min, int max)
      throws ExceptionFonctionnelle {
    List<Ville> villes = villeDao.findVillesByPopulationEntre(min, max);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Insère une nouvelle ville en base, après vérification qu'aucune ville du même nom n'existe
   * déjà.
   *
   * @param ville les données de la ville à créer (l'identifiant éventuellement fourni est ignoré,
   *              il sera généré par la base)
   * @return la liste des villes après insertion
   * @throws ExceptionFonctionnelle si une ville du même nom existe déjà
   */
  @Transactional
  public List<Ville> insertVille(Ville ville) throws ExceptionFonctionnelle {
    if (villeDao.existsVilleByNom(ville.getNom())) {
      throw new ExceptionFonctionnelle("La ville existe déjà");
    }
    Ville nouvelleVille = new Ville();
    nouvelleVille.setNom(ville.getNom());
    nouvelleVille.setPopulation(ville.getPopulation());
    villeDao.persist(nouvelleVille);
    return villeDao.findVilles();
  }

  /**
   * Modifie les données d'une ville existante.
   *
   * @param idVille       l'identifiant de la ville à modifier
   * @param villeModifiee les nouvelles données à appliquer (nom, population)
   * @return la liste des villes après modification
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  @Transactional
  public List<Ville> modifierVille(int idVille, Ville villeModifiee) throws ExceptionFonctionnelle {
    if (villeDao.findById(idVille) == null) {
      throw new ExceptionFonctionnelle("Ville non trouvée");
    }
    villeDao.merge(idVille, villeModifiee);
    return villeDao.findVilles();
  }

  /**
   * Supprime une ville existante.
   *
   * @param idVille l'identifiant de la ville à supprimer
   * @return la liste des villes après suppression
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  @Transactional
  public List<Ville> supprimerVille(int idVille) throws ExceptionFonctionnelle {
    Ville ville = villeDao.findById(idVille);
    if (ville == null) {
      throw new ExceptionFonctionnelle("Ville non trouvée");
    }
    villeDao.remove(ville);
    return villeDao.findVilles();
  }

}