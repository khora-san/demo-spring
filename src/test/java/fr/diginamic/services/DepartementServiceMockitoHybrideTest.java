package fr.diginamic.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import fr.diginamic.entities.Departement;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.repository.DepartementRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class DepartementServiceMockitoHybrideTest {

  @Autowired
  private DepartementService departementService;

  @MockitoBean
  private DepartementRepository departementRepository;

  @Test
  void extractDepartementByCode_devraitRetournerLeDepartement() throws ExceptionFonctionnelle {
    Departement departement = new Departement();
    departement.setCode("34");
    departement.setNom("Herault");

    when(departementRepository.findByCode("34")).thenReturn(Optional.of(departement));

    Departement resultat = departementService.extractDepartementByCode("34");

    assertEquals("Herault", resultat.getNom());
  }
}