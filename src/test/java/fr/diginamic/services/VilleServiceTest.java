package fr.diginamic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests de VilleService s'appuyant sur le jeu de données fixe défini dans
 * src/test/resources/data.sql :
 * <p>
 * Departement 34 (id 900) : Montpellier (300000), Sete (45000), Beziers (80000)<br> Departement 75
 * (id 901) : Paris (2000000)
 * <p>
 * Chaque test s'exécute dans une transaction annulée à la fin ({@code @Transactional} au niveau de
 * la classe), donc les insertions/modifications/suppressions faites par un test n'affectent jamais
 * les autres tests ni le jeu de données de base.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VilleServiceTest {

  @Autowired
  private VilleService villeService;

  @Test
  void extractVilles_devraitRetournerToutesLesVillesDuJeuDeDonnees() {
    Page<Ville> villes = villeService.extractVilles(Pageable.unpaged());

    assertEquals(4, villes.getTotalElements());
  }

  @Test
  void extractVille_devraitRetournerMontpellier() throws ExceptionFonctionnelle {
    Ville ville = villeService.extractVille(900);

    assertEquals("Montpellier", ville.getNom());
  }

  @Test
  void extractVille_devraitLeverUneExceptionSiIdInconnu() {
    assertThrows(ExceptionFonctionnelle.class, () -> villeService.extractVille(999999));
  }

  @Test
  void extractVillesByNameStartWith_devraitTrouverMontpellier() throws ExceptionFonctionnelle {
    List<Ville> villes = villeService.extractVillesByNameStartWith("Mont");

    assertEquals(1, villes.size());
    assertEquals("Montpellier", villes.get(0).getNom());
  }

  @Test
  void extractVillesByNameStartWith_devraitLeverUneExceptionSiAucuneCorrespondance() {
    assertThrows(ExceptionFonctionnelle.class,
        () -> villeService.extractVillesByNameStartWith("Xyz"));
  }

  @Test
  void extractVillesByDepartementCode_devraitRetournerLesTroisVillesDuDepartement34()
      throws ExceptionFonctionnelle {
    List<Ville> villes = villeService.extractVillesByDepartementCode("34");

    assertEquals(3, villes.size());
  }

  @Test
  void extractVillesByDepartementCode_devraitLeverUneExceptionSiCodeInconnu() {
    assertThrows(ExceptionFonctionnelle.class,
        () -> villeService.extractVillesByDepartementCode("XX"));
  }

  @Test
  void extractVillesByPopulationSuperieure_devraitRetournerLesVillesAuDessusDuSeuil()
      throws ExceptionFonctionnelle {
    List<Ville> villes = villeService.extractVillesByPopulationSuperieure(50000);

    assertEquals(3, villes.size());
    assertEquals("Paris", villes.get(0).getNom());
  }

  @Test
  void extractVillesByPopulationEntre_devraitRetournerLesVillesDansLIntervalle()
      throws ExceptionFonctionnelle {
    List<Ville> villes = villeService.extractVillesByPopulationEntre(50000, 500000);

    assertEquals(2, villes.size());
  }

  @Test
  void extractTopVillesByDepartementCode_devraitRetournerLaVilleLaPlusPeupleeDuDepartement34()
      throws ExceptionFonctionnelle {
    List<Ville> top = villeService.extractTopVillesByDepartementCode("34", 1);

    assertEquals(1, top.size());
    assertEquals("Montpellier", top.get(0).getNom());
  }

  @Test
  void extractVillesByPopulationSuperieureAndDepartementCode_devraitFiltrerCorrectement()
      throws ExceptionFonctionnelle {
    List<Ville> villes =
        villeService.extractVillesByPopulationSuperieureAndDepartementCode("34", 50000);

    assertEquals(2, villes.size());
  }

  @Test
  void extractVillesByPopulationEntreAndDepartementCode_devraitFiltrerCorrectement()
      throws ExceptionFonctionnelle {
    List<Ville> villes =
        villeService.extractVillesByPopulationEntreAndDepartementCode("34", 50000, 100000);

    assertEquals(1, villes.size());
    assertEquals("Beziers", villes.get(0).getNom());
  }

  @Test
  @WithMockUser
  void insertVille_devraitAjouterUneNouvelleVilleDansLeDepartement() throws ExceptionFonctionnelle {
    Ville nouvelleVille = new Ville();
    nouvelleVille.setNom("Nimes");
    nouvelleVille.setPopulation(150000);

    villeService.insertVille(nouvelleVille, "34", null);

    List<Ville> villes = villeService.extractVillesByDepartementCode("34");
    assertEquals(4, villes.size());
  }

  @Test
  @WithMockUser
  void insertVille_devraitEchouerSiLeNomExisteDejaDansLeMemeDepartement() {
    Ville villeDupliquee = new Ville();
    villeDupliquee.setNom("Paris");
    villeDupliquee.setPopulation(1000);

    assertThrows(ExceptionFonctionnelle.class,
        () -> villeService.insertVille(villeDupliquee, "75", null));
  }

  @Test
  @WithMockUser
  void insertVille_devraitReussirSiLeNomExisteDejaMaisDansUnAutreDepartement()
      throws ExceptionFonctionnelle {
    Ville villeMemeNomAutreDepartement = new Ville();
    villeMemeNomAutreDepartement.setNom("Paris");
    villeMemeNomAutreDepartement.setPopulation(1000);

    villeService.insertVille(villeMemeNomAutreDepartement, "34", null);

    List<Ville> villesDept34 = villeService.extractVillesByDepartementCode("34");
    assertEquals(4, villesDept34.size());
  }

  @Test
  @WithMockUser
  void modifierVille_devraitMettreAJourLaPopulationDeSete() throws ExceptionFonctionnelle {
    Ville villeModifiee = new Ville();
    villeModifiee.setNom("Sete");
    villeModifiee.setPopulation(50000);

    villeService.modifierVille(901, villeModifiee, "34", null);

    Ville sete = villeService.extractVille(901);
    assertEquals(50000, sete.getPopulation());
  }

  @Test
  void supprimerVille_devraitRetirerBeziersDeLaBase() throws ExceptionFonctionnelle {
    villeService.supprimerVille(902);

    assertThrows(ExceptionFonctionnelle.class, () -> villeService.extractVille(902));
  }
}