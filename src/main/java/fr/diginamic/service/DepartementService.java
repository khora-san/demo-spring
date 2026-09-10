package fr.diginamic.service;

import fr.diginamic.dto.DepartementApiDto;
import fr.diginamic.entity.Departement;
import fr.diginamic.exception.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import fr.diginamic.util.PropertiesFileUtils;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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

  private static final Logger log = LoggerFactory.getLogger(DepartementService.class);

  @Value("${application.init}")
  private boolean applicationInit;

  private final RestTemplate restTemplate = new RestTemplate();

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
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Departement nouveauDepartement = new Departement();
    nouveauDepartement.setCode(departement.getCode());
    nouveauDepartement.setNom(departement.getNom());
    nouveauDepartement.setUserMaj(username);
    nouveauDepartement.setDateMaj(LocalDateTime.now());
    log.info("Département '{}' inséré par {} le {}", nouveauDepartement.getNom(), username, nouveauDepartement.getDateMaj());
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
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    departementExistant.setCode(departementModifie.getCode());
    departementExistant.setNom(departementModifie.getNom());
    departementExistant.setUserMaj(username);
    departementExistant.setDateMaj(LocalDateTime.now());
    log.info("Département '{}' modifié par {} le {}", departementExistant.getNom(), username, departementExistant.getDateMaj());
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
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Departement departement = departementRepository.findById(idDepartement)
        .orElseThrow(() -> new ExceptionFonctionnelle("Département non trouvé"));
    log.info("Département '{}' supprimé par {} le {}", departement.getNom(), username, LocalDateTime.now());
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

  @PostConstruct
  public void initData() {
    if (!applicationInit) {
      log.info("Initialisation des départements ignorée (application.init=false)");
      return;
    }

    // appel API et désérialisation
    DepartementApiDto[] dtos = restTemplate.getForObject("https://geo.api.gouv.fr/departements",
        DepartementApiDto[].class);

    // pour chaque département reçu, retrouver l'entité correspondante
    // en base par son code, et mettre à jour son nom
    for (DepartementApiDto dto : dtos) {
      departementRepository.findByCode(dto.code()).ifPresent(departement -> {
        departement.setNom(dto.nom());
        departementRepository.save(departement);
      });
    }
    // repasser application.init à false dans le fichier application.properties
    PropertiesFileUtils.updateProperty("src/main/resources/application.properties",
        "application.init", "false");

  }
}