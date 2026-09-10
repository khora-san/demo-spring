package fr.diginamic.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.entity.Departement;
import fr.diginamic.entity.Ville;
import fr.diginamic.repository.DepartementRepository;
import fr.diginamic.repository.VilleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
class VilleControleurTest {

  @Autowired
  private JsonMapper jsonMapper;

  @MockitoBean
  private VilleRepository villeRepository;

  @MockitoBean
  private DepartementRepository departementRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void addVille_devraitCreerUneVilleAvecPayloadValide() throws Exception {
    // Département déjà existant, retourné par le repository mocké
    Departement departementExistant = new Departement();
    departementExistant.setCode("34");
    departementExistant.setNom("Herault");
    when(departementRepository.findByCode("34")).thenReturn(Optional.of(departementExistant));

    // Aucune ville homonyme dans ce département -> la création peut avoir lieu
    when(villeRepository.existsByNomAndDepartementCode("Montpellier", "34")).thenReturn(false);

    // Ville telle qu'elle existerait après sauvegarde, utilisée pour simuler le retour de findAll()
    Ville villeCree = new Ville();
    villeCree.setNom("Montpellier");
    villeCree.setPopulation(315000);
    villeCree.setDepartement(departementExistant);
    when(villeRepository.findAll()).thenReturn(List.of(villeCree));

    // Payload envoyé dans le corps de la requête POST
    VilleDto villeDto = new VilleDto(null, "Montpellier", 315000, "34", null);

    // Appel HTTP simulé + vérification du statut et du contenu de la réponse
    this.mockMvc.perform(post("/villes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(villeDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nom").value("Montpellier"));
  }

  @Test
  void addVille_devraitRejeterUnPayloadInvalide() throws Exception {
    // Nom vide -> viole @NotBlank sur VilleDto.nom, doit être rejeté avant d'atteindre le service
    VilleDto villeDtoInvalide = new VilleDto(null, "", 315000, "34", null);

    this.mockMvc.perform(post("/villes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonMapper.writeValueAsString(villeDtoInvalide)))
        .andExpect(status().isBadRequest());

    // Preuve que la validation a bien bloqué la requête en amont :
    // le repository n'a jamais été sollicité pour une sauvegarde
    verify(villeRepository, never()).save(any(Ville.class));
  }
}
