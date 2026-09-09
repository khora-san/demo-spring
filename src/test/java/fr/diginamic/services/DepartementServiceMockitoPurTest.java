package fr.diginamic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.diginamic.entities.Departement;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class DepartementServiceMockitoPurTest {

  @Mock
  private DepartementRepository departementRepository;

  @InjectMocks
  private DepartementService departementService;

  @BeforeEach
  void setUpSecurityContext() {
    Authentication authentication = new UsernamePasswordAuthenticationToken("testuser", null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void extractDepartementByCode_devraitRetournerLeDepartement() throws ExceptionFonctionnelle {
    Departement departement = new Departement();
    departement.setCode("34");
    departement.setNom("Herault");

    when(departementRepository.findByCode("34")).thenReturn(Optional.of(departement));

    Departement resultat = departementService.extractDepartementByCode("34");

    assertEquals("Herault", resultat.getNom());
  }

  @Test
  void extractDepartementByCode_devraitLeverUneExceptionSiCodeInconnu() {
    when(departementRepository.findByCode("XX")).thenReturn(Optional.empty());

    assertThrows(ExceptionFonctionnelle.class,
        () -> departementService.extractDepartementByCode("XX"));
  }

  @Test
  void insertDepartement_devraitInsererUnNouveauDepartement() throws ExceptionFonctionnelle {
    when(departementRepository.findByCode("34")).thenReturn(Optional.empty());
    when(departementRepository.save(any(Departement.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Departement departementAInserer = new Departement();
    departementAInserer.setCode("34");
    departementAInserer.setNom("Herault");

    Departement resultat = departementService.insertDepartement(departementAInserer);

    assertEquals("34", resultat.getCode());
    assertEquals("Herault", resultat.getNom());
  }

  @Test
  void modifierDepartement_devraitMettreAJourCodeEtNom() throws ExceptionFonctionnelle {
    // Given : l'entité existante en base (simulée), et les nouvelles valeurs souhaitées
    Departement departementExistant = new Departement();
    departementExistant.setCode("34");
    departementExistant.setNom("Herault");

    Departement departementModifie = new Departement();
    departementModifie.setCode("34B");
    departementModifie.setNom("Herault-Bis");

    when(departementRepository.findById(1)).thenReturn(Optional.of(departementExistant));

    // When
    Departement resultat = departementService.modifierDepartement(1, departementModifie);

    // Then : la recopie des champs a bien été appliquée
    assertEquals("34B", resultat.getCode());
    assertEquals("Herault-Bis", resultat.getNom());
  }

  @Test
  void modifierDepartement_devraitLeverUneExceptionSiIdInconnu() {
    when(departementRepository.findById(999)).thenReturn(Optional.empty());

    Departement departementModifie = new Departement();
    departementModifie.setCode("34B");
    departementModifie.setNom("Herault-Bis");

    assertThrows(ExceptionFonctionnelle.class,
        () -> departementService.modifierDepartement(999, departementModifie));
  }

  @Test
  void supprimerDepartement_devraitSupprimerUnDepartementExistant() throws ExceptionFonctionnelle {
    Departement departementExistant = new Departement();
    departementExistant.setCode("34");
    departementExistant.setNom("Herault");
    when(departementRepository.findById(1)).thenReturn(Optional.of(departementExistant));

    departementService.supprimerDepartement(1);

    verify(departementRepository).delete(departementExistant);

  }

  @Test
  void supprimerDepartement_devraitLeverUneExceptionSiIdInconnu() throws ExceptionFonctionnelle {
    when(departementRepository.findById(1)).thenReturn(Optional.empty());

    assertThrows(ExceptionFonctionnelle.class,
        () -> departementService.supprimerDepartement(1));

    verify(departementRepository, never()).delete(any(Departement.class));
  }
}