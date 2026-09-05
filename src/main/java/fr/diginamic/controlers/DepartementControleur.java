package fr.diginamic.controlers;

import fr.diginamic.dto.DepartementDto;
import fr.diginamic.entities.Departement;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.mapper.DepartementMapper;
import fr.diginamic.services.DepartementService;
import fr.diginamic.utils.DepartementPdfExporter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.text.DocumentException;
import fr.diginamic.entities.Ville;
import fr.diginamic.services.VilleService;

/**
 * Contrôleur REST exposant les opérations CRUD sur les départements. Traduit les échanges HTTP
 * (DTO, codes de retour) et délègue la logique métier à DepartementService, la conversion
 * entité/DTO à DepartementMapper.
 */
@RestController
@RequestMapping("/departements")
public class DepartementControleur implements DepartementControleurDoc {

  private final DepartementService departementService;
  private final VilleService villeService;
  private final DepartementMapper departementMapper;

  /**
   * Construit le contrôleur en injectant le service métier et le mapper nécessaires à la conversion
   * entre entités Departement et DepartementDto.
   *
   * @param departementService service portant la logique métier sur les départements
   * @param departementMapper  mapper assurant la conversion entité/DTO
   */
  public DepartementControleur(DepartementService departementService, VilleService villeService,
      DepartementMapper departementMapper) {
    this.departementService = departementService;
    this.villeService = villeService;
    this.departementMapper = departementMapper;
  }


  @Override
  @GetMapping
  public ResponseEntity<List<DepartementDto>> getDepartements() {
    List<DepartementDto> departementDtos = departementService.extractDepartements().stream()
        .map(departementMapper::toDto)
        .toList();
    return ResponseEntity.ok(departementDtos);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DepartementDto> getDepartementById(@PathVariable int id)
      throws ExceptionFonctionnelle {
    Departement departement = departementService.extractDepartement(id);
    return ResponseEntity.ok(departementMapper.toDto(departement));
  }

  @Override
  @GetMapping(params = "code")
  public ResponseEntity<DepartementDto> getDepartementByCode(@RequestParam String code)
      throws ExceptionFonctionnelle {
    Departement departement = departementService.extractDepartementByCode(code);
    return ResponseEntity.ok(departementMapper.toDto(departement));
  }

  @Override
  @PostMapping
  public ResponseEntity<DepartementDto> createDepartement(
      @Valid @RequestBody DepartementDto departementDto)
      throws ExceptionFonctionnelle {
    Departement departement = departementMapper.toEntity(departementDto);
    Departement departementCree = departementService.insertDepartement(departement);
    return ResponseEntity.ok(departementMapper.toDto(departementCree));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<DepartementDto> updateDepartementById(@PathVariable int id,
      @Valid @RequestBody DepartementDto departementDto)
      throws ExceptionFonctionnelle {
    Departement departement = departementMapper.toEntity(departementDto);
    Departement departementModifie = departementService.modifierDepartement(id, departement);
    return ResponseEntity.ok(departementMapper.toDto(departementModifie));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteDepartementById(@PathVariable int id)
      throws ExceptionFonctionnelle {
    departementService.supprimerDepartement(id);
    return ResponseEntity.ok().build();
  }

  //
  //
  @GetMapping("/{code}/export")
  public void exportDepartementPdf(@PathVariable String code, HttpServletResponse response)
      throws IOException, DocumentException, ExceptionFonctionnelle {
    Departement departement = departementService.extractDepartementByCode(code);
    List<Ville> villes = villeService.extractVillesByDepartementCode(code);

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition",
        "attachment; filename=\"departement-" + departement.getCode() + ".pdf\"");

    DepartementPdfExporter.export(departement, villes, response.getOutputStream());
  }
}