package fr.diginamic.services;

import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.repository.VilleRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service métier pour la gestion des villes.
 * <p>
 * Cette classe centralise les règles métier (unicité du nom, existence d'une ville, résolution du
 * département associé) et orchestre les appels à {@link VilleRepository}, l'interface Spring Data
 * JPA qui gère l'accès aux données des villes.
 */
@Service
public class VilleService {

  private final VilleRepository villeRepository;
  private final DepartementService departementService;

  /**
   * Construit le service en lui injectant ses dépendances.
   *
   * @param villeRepository    le repository Spring Data JPA utilisé pour accéder aux données des
   *                           villes
   * @param departementService le service utilisé pour résoudre ou créer le département associé à
   *                           une ville
   */
  public VilleService(VilleRepository villeRepository, DepartementService departementService) {
    this.villeRepository = villeRepository;
    this.departementService = departementService;
  }

  /**
   * Extrait les villes présentes en base, de façon paginée.
   *
   * @param pageable les paramètres de pagination (numéro de page, taille, tri)
   * @return la page de villes correspondante
   */
  public Page<Ville> extractVilles(Pageable pageable) {
    return villeRepository.findAll(pageable);
  }

  /**
   * Extrait la ville correspondant à l'identifiant donné.
   *
   * @param id l'identifiant de la ville recherchée
   * @return la ville correspondante
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  public Ville extractVille(int id) throws ExceptionFonctionnelle {
    return villeRepository.findById(id)
        .orElseThrow(() -> new ExceptionFonctionnelle("Ville non trouvée"));
  }

  /**
   * Extrait les villes dont le nom commence par le préfixe donné.
   *
   * @param prefixe le préfixe recherché dans le nom des villes
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne correspond au préfixe
   */
  public List<Ville> extractVillesByNameStartWith(String prefixe) throws ExceptionFonctionnelle {
    List<Ville> villes = villeRepository.findByNomStartingWith(prefixe);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Extrait les villes dont la population est strictement supérieure au minimum donné, triées par
   * population décroissante.
   *
   * @param min le seuil minimum de population (exclu)
   * @return la liste des villes correspondantes, triées par population décroissante
   * @throws ExceptionFonctionnelle si aucune ville ne dépasse ce seuil
   */
  public List<Ville> extractVillesByPopulationSuperieure(int min) throws ExceptionFonctionnelle {
    List<Ville> villes = villeRepository.findByPopulationGreaterThanOrderByPopulationDesc(min);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Extrait les villes dont la population est strictement comprise entre les bornes données, triées
   * par population décroissante.
   *
   * @param min la population minimum (exclue)
   * @param max la population maximum (exclue)
   * @return la liste des villes correspondantes, triées par population décroissante
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet intervalle
   */
  public List<Ville> extractVillesByPopulationEntre(int min, int max)
      throws ExceptionFonctionnelle {
    List<Ville> villes =
        villeRepository.findByPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(min,
            max);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville correspondante");
    }
    return villes;
  }

  /**
   * Extrait les n premières villes (par population décroissante) d'un département donné.
   *
   * @param code le code du département
   * @param n    le nombre de villes affichées
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si le code de département est invalide, ou si aucune ville n'est
   *                                trouvée pour ce département
   */
  public List<Ville> extractTopVillesByDepartementCode(String code, int n)
      throws ExceptionFonctionnelle {
    departementService.extractDepartementByCode(code);
    List<Ville> villes = villeRepository.findByDepartementCodeOrderByPopulationDesc(code,
        PageRequest.of(0, n));
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville trouvée pour ce département");
    }
    return villes;
  }

  /**
   * Extrait les villes d'un département dont la population est supérieure à un seuil donné, triées
   * par population décroissante.
   *
   * @param code code du département
   * @param min  population minimale (strictement)
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si le département n'existe pas ou si aucune ville ne correspond
   */
  public List<Ville> extractVillesByPopulationSuperieureAndDepartementCode(String code, int min)
      throws ExceptionFonctionnelle {
    departementService.extractDepartementByCode(code);
    List<Ville> villes =
        villeRepository.findByDepartementCodeAndPopulationGreaterThanOrderByPopulationDesc(code,
            min);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville trouvée pour ce département");
    }
    return villes;
  }

  /**
   * Extrait les villes dont la population est strictement comprise entre un min et un max, pour un
   * département donné, triées par population décroissante.
   *
   * @param code le code du département
   * @param min  la population minimum (exclue)
   * @param max  la population maximum (exclue)
   * @return la liste des villes correspondantes, triées par population décroissante
   * @throws ExceptionFonctionnelle si le code de département est invalide, ou si aucune ville n'est
   *                                trouvée pour ces critères
   */
  public List<Ville> extractVillesByPopulationEntreAndDepartementCode(String code, int min,
      int max) throws ExceptionFonctionnelle {
    departementService.extractDepartementByCode(code);
    List<Ville> villes =
        villeRepository.findByDepartementCodeAndPopulationGreaterThanAndPopulationLessThanOrderByPopulationDesc(
            code, min, max);
    if (villes.isEmpty()) {
      throw new ExceptionFonctionnelle("Aucune ville trouvée pour ce département");
    }
    return villes;
  }

  /**
   * Insère une nouvelle ville en base, après vérification qu'aucune ville du même nom n'existe
   * déjà. Le département associé est résolu à partir du code et/ou de l'identifiant fournis (voir
   * {@link DepartementService#resolveDepartement}) — s'il n'existe pas encore et qu'un code est
   * fourni, il est créé automatiquement.
   *
   * @param ville           les données de la ville à créer (l'identifiant éventuellement fourni est
   *                        ignoré, il sera généré par la base)
   * @param codeDepartement code du département associé (peut être nul si idDepartement est
   *                        renseigné)
   * @param idDepartement   identifiant du département associé (peut être nul si codeDepartement est
   *                        renseigné)
   * @return la liste des villes après insertion
   * @throws ExceptionFonctionnelle si une ville du même nom existe déjà, ou si le département ne
   *                                peut être résolu ni créé
   */
  @Transactional
  public List<Ville> insertVille(Ville ville, String codeDepartement, Integer idDepartement)
      throws ExceptionFonctionnelle {
    if (villeRepository.existsByNom(ville.getNom())) {
      throw new ExceptionFonctionnelle("La ville existe déjà");
    }
    Departement departement = departementService.resolveDepartement(codeDepartement, idDepartement);
    Ville nouvelleVille = new Ville();
    nouvelleVille.setNom(ville.getNom());
    nouvelleVille.setPopulation(ville.getPopulation());
    nouvelleVille.setDepartement(departement);
    villeRepository.save(nouvelleVille);
    return villeRepository.findAll();
  }

  /**
   * Modifie les données d'une ville existante. Le département associé est résolu à partir du code
   * et/ou de l'identifiant fournis (voir {@link DepartementService#resolveDepartement}) — s'il
   * n'existe pas encore et qu'un code est fourni, il est créé automatiquement.
   *
   * @param idVille         l'identifiant de la ville à modifier
   * @param villeModifiee   les nouvelles données à appliquer (nom, population)
   * @param codeDepartement code du département associé (peut être nul si idDepartement est
   *                        renseigné)
   * @param idDepartement   identifiant du département associé (peut être nul si codeDepartement est
   *                        renseigné)
   * @return la liste des villes après modification
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant, ou si le
   *                                département ne peut être résolu ni créé
   */
  @Transactional
  public List<Ville> modifierVille(int idVille, Ville villeModifiee, String codeDepartement,
      Integer idDepartement) throws ExceptionFonctionnelle {
    Ville villeExistante = villeRepository.findById(idVille)
        .orElseThrow(() -> new ExceptionFonctionnelle("Ville non trouvée"));
    Departement departement = departementService.resolveDepartement(codeDepartement, idDepartement);
    // Entité gérée par le contexte de persistance : la mise à jour des champs suffit, la
    // synchronisation en base se fait par dirty-checking à la fin de la transaction.
    villeExistante.setNom(villeModifiee.getNom());
    villeExistante.setPopulation(villeModifiee.getPopulation());
    villeExistante.setDepartement(departement);
    return villeRepository.findAll();
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
    Ville ville = villeRepository.findById(idVille)
        .orElseThrow(() -> new ExceptionFonctionnelle("Ville non trouvée"));
    villeRepository.delete(ville);
    return villeRepository.findAll();
  }
}