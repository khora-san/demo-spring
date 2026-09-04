package fr.diginamic.services;

import fr.diginamic.entities.Departement;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fournit la logique métier relative aux départements : consultation, création, modification,
 * suppression, ainsi que la résolution du département associé à une ville.
 */
@Service
public class DepartementService {

  private final DepartementRepository departementRepository;

  /**
   * Construit le service à partir du repository des départements.
   *
   * @param departementRepository repository utilisé pour l'accès aux données des départements
   */
  public DepartementService(DepartementRepository departementRepository) {
    this.departementRepository = departementRepository;
  }

  /**
   * Extrait l'ensemble des départements existants.
   *
   * @return la liste de tous les départements
   */
  public List<Departement> extractDepartements() {
    return departementRepository.findAll();
  }

  /**
   * Extrait le département correspondant à l'identifiant donné.
   *
   * @param id identifiant du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne correspond à cet identifiant
   */
  public Departement extractDepartement(int id) throws ExceptionFonctionnelle {
    return departementRepository.findById(id)
        .orElseThrow(() -> new ExceptionFonctionnelle("Département non trouvé"));
  }

  /**
   * Extrait le département correspondant au code donné.
   *
   * @param code code du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne correspond à ce code
   */
  public Departement extractDepartementByCode(String code) throws ExceptionFonctionnelle {
    return departementRepository.findByCode(code)
        .orElseThrow(() -> new ExceptionFonctionnelle("Département non trouvé"));
  }

  /**
   * Insère un nouveau département.
   *
   * @param departement département à insérer (seuls le code et le nom sont pris en compte)
   * @return le département inséré, tel que persisté
   * @throws ExceptionFonctionnelle si un département portant le même code existe déjà
   */
  @Transactional
  public Departement insertDepartement(Departement departement) throws ExceptionFonctionnelle {
    if (departementRepository.findByCode(departement.getCode()).isPresent()) {
      throw new ExceptionFonctionnelle("Le département existe déjà");
    }
    Departement nouveauDepartement = new Departement();
    nouveauDepartement.setCode(departement.getCode());
    nouveauDepartement.setNom(departement.getNom());
    return departementRepository.save(nouveauDepartement);
  }

  /**
   * Modifie le département correspondant à l'identifiant donné.
   *
   * @param idDepartement      identifiant du département à modifier
   * @param departementModifie département contenant les nouvelles valeurs (code et nom)
   * @return le département modifié
   * @throws ExceptionFonctionnelle si aucun département ne correspond à cet identifiant
   */
  @Transactional
  public Departement modifierDepartement(int idDepartement, Departement departementModifie)
      throws ExceptionFonctionnelle {
    Departement departementExistant = departementRepository.findById(idDepartement)
        .orElseThrow(() -> new ExceptionFonctionnelle("Département non trouvé"));
    departementExistant.setCode(departementModifie.getCode());
    departementExistant.setNom(departementModifie.getNom());
    return departementExistant;
  }

  /**
   * Supprime le département correspondant à l'identifiant donné.
   *
   * @param idDepartement identifiant du département à supprimer
   * @throws ExceptionFonctionnelle si aucun département ne correspond à cet identifiant
   */
  @Transactional
  public void supprimerDepartement(int idDepartement) throws ExceptionFonctionnelle {
    Departement departement = departementRepository.findById(idDepartement)
        .orElseThrow(() -> new ExceptionFonctionnelle("Département non trouvé"));
    departementRepository.delete(departement);
  }

  /**
   * Résout le département associé à une ville à partir d'un identifiant et/ou d'un code.
   * L'identifiant est prioritaire s'il est fourni et valide. Si seul le code est fourni (ou si
   * l'identifiant fourni est invalide) et qu'aucun département ne correspond à ce code, un nouveau
   * département est créé avec ce seul code.
   *
   * @param codeDepartement code du département, utilisé en secours ou pour la création
   * @param idDepartement   identifiant du département, prioritaire s'il est fourni
   * @return le département résolu (existant ou nouvellement créé)
   * @throws ExceptionFonctionnelle si ni l'identifiant ni le code ne sont fournis
   */
  @Transactional
  public Departement resolveDepartement(String codeDepartement, Integer idDepartement)
      throws ExceptionFonctionnelle {
    if (idDepartement != null) {
      try {
        return extractDepartement(idDepartement);
      } catch (ExceptionFonctionnelle e) {
        // id fourni mais invalide : on retente via le code, s'il est disponible
      }
    }
    if (codeDepartement != null) {
      try {
        return extractDepartementByCode(codeDepartement);
      } catch (ExceptionFonctionnelle e) {
        Departement nouveauDepartement = new Departement();
        nouveauDepartement.setCode(codeDepartement);
        return insertDepartement(nouveauDepartement);
      }
    }
    throw new ExceptionFonctionnelle("Département inconnu");
  }
}