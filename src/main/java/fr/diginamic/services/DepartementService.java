package fr.diginamic.services;

import fr.diginamic.dao.DepartementDao;
import fr.diginamic.entities.Departement;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service contenant la logique métier relative aux départements. Travaille exclusivement avec des
 * entités ; la conversion vers/depuis les DTO est de la responsabilité de la couche
 * contrôleur/mapper.
 */
@Service
public class DepartementService {

  private final DepartementDao departementDao;

  public DepartementService(DepartementDao departementDao) {
    this.departementDao = departementDao;
  }

  /**
   * Récupère l'ensemble des départements.
   *
   * @return la liste de tous les départements
   */
  public List<Departement> extractDepartements() {
    return departementDao.findDepartements();
  }

  /**
   * Récupère un département à partir de son identifiant.
   *
   * @param id identifiant du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne possède cet identifiant
   */
  public Departement extractDepartement(int id) throws ExceptionFonctionnelle {
    Departement departement = departementDao.findById(id);
    if (departement == null) {
      throw new ExceptionFonctionnelle("Département non trouvé");
    }
    return departement;
  }

  /**
   * Récupère un département à partir de son code.
   *
   * @param code code du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne possède ce code
   */
  public Departement extractDepartementByCode(String code) throws ExceptionFonctionnelle {
    Departement departement = departementDao.findByCode(code);
    if (departement == null) {
      throw new ExceptionFonctionnelle("Département non trouvé");
    }
    return departement;
  }

  /**
   * Crée un nouveau département. Un nouvel objet est reconstruit à partir des seules données utiles
   * (code, nom) afin de ne pas faire confiance à un éventuel identifiant fourni par le client.
   *
   * @param departement département à créer
   * @return le département créé, avec son identifiant généré
   * @throws ExceptionFonctionnelle si un département possède déjà ce code
   */
  @Transactional
  public Departement insertDepartement(Departement departement) throws ExceptionFonctionnelle {
    if (departementDao.findByCode(departement.getCode()) != null) {
      throw new ExceptionFonctionnelle("Le département existe déjà");
    }
    Departement newDepartement = new Departement();
    newDepartement.setCode(departement.getCode());
    newDepartement.setNom(departement.getNom());
    departementDao.persist(newDepartement);
    return newDepartement;
  }

  /**
   * Modifie un département existant.
   *
   * @param idDepartement      identifiant du département à modifier
   * @param departementModifie département contenant les nouvelles valeurs
   * @return le département modifié
   * @throws ExceptionFonctionnelle si aucun département ne possède cet identifiant
   */
  @Transactional
  public Departement modifierDepartement(int idDepartement, Departement departementModifie)
      throws ExceptionFonctionnelle {
    if (departementDao.findById(idDepartement) == null) {
      throw new ExceptionFonctionnelle("Département non trouvé");
    }
    return departementDao.merge(idDepartement, departementModifie);
  }

  /**
   * Supprime un département.
   *
   * @param idDepartement identifiant du département à supprimer
   * @throws ExceptionFonctionnelle si aucun département ne possède cet identifiant
   */
  @Transactional
  public void supprimerDepartement(int idDepartement) throws ExceptionFonctionnelle {
    Departement departement = departementDao.findById(idDepartement);
    if (departement == null) {
      throw new ExceptionFonctionnelle("Département non trouvé");
    }
    departementDao.remove(departement);
  }

  /**
   * Résout le département associé à une ville à partir d'un code et/ou d'un identifiant. Tente
   * d'abord la résolution par identifiant, puis par code. Si le code est renseigné mais ne
   * correspond à aucun département existant, un nouveau département est créé automatiquement avec
   * ce seul code.
   *
   * @param codeDepartement code du département (peut être nul si idDepartement est renseigné)
   * @param idDepartement   identifiant du département (peut être nul si codeDepartement est
   *                        renseigné)
   * @return le département résolu, existant ou nouvellement créé
   * @throws ExceptionFonctionnelle si aucun département ne peut être résolu ni créé (identifiant
   *                                inconnu et aucun code fourni)
   */
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
        // code fourni mais aucun département existant : on le crée
        Departement nouveauDepartement = new Departement();
        nouveauDepartement.setCode(codeDepartement);
        return insertDepartement(nouveauDepartement);
      }
    }
    throw new ExceptionFonctionnelle("Département inconnu");
  }
}