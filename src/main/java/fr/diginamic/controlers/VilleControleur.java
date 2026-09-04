package fr.diginamic.controlers;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import fr.diginamic.mapper.VilleMapper;
import fr.diginamic.services.VilleService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur implements VilleControleurDoc {

  private final VilleService villeService;
  private final VilleMapper villeMapper;

  public VilleControleur(VilleService villeService, VilleMapper villeMapper) {
    this.villeService = villeService;
    this.villeMapper = villeMapper;
  }

  @Override
  @GetMapping
  public ResponseEntity<List<VilleDto>> getVilles() {
    List<VilleDto> villesDto = villeService.extractVilles().stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<VilleDto> getVilleById(@PathVariable int id) throws ExceptionFonctionnelle {
    Ville ville = villeService.extractVille(id);
    return ResponseEntity.ok(villeMapper.toDto(ville));
  }

  @Override
  @GetMapping(value = "/recherche", params = "prefixe")
  public ResponseEntity<List<VilleDto>> getVillesByNameStartWith(@RequestParam String prefixe)
      throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.extractVillesByNameStartWith(prefixe).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @GetMapping(value = "/recherche", params = {"min", "!max", "!code"})
  public ResponseEntity<List<VilleDto>> getVillesByPopGreaterTo(@RequestParam int min)
      throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.extractVillesByPopulationSuperieure(min).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @GetMapping(value = "/recherche", params = {"min", "max", "!code"})
  public ResponseEntity<List<VilleDto>> getVillesByPopWithin(@RequestParam int min,
      @RequestParam int max)
      throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.extractVillesByPopulationEntre(min, max).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @GetMapping(value = "/recherche", params = {"code", "n"})
  public ResponseEntity<List<VilleDto>> getTopVillesByDepartementCode(
      @RequestParam String code, @RequestParam int n) throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.extractTopVillesByDepartementCode(code, n).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @GetMapping(value = "/recherche", params = {"code", "min", "max"})
  public ResponseEntity<List<VilleDto>> getVillesByPopulationEntreAndDepartementCode(
      @RequestParam String code, @RequestParam int min, @RequestParam int max)
      throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.extractVillesByPopulationEntreAndDepartementCode(code,
            min, max).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @PostMapping
  public ResponseEntity<List<VilleDto>> addVille(@Valid @RequestBody VilleDto villeDto)
      throws ExceptionFonctionnelle {
    Ville ville = villeMapper.toEntity(villeDto);
    List<VilleDto> villesDto = villeService.insertVille(ville, villeDto.codeDepartement(),
            villeDto.idDepartement()).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<List<VilleDto>> putVilleById(@PathVariable int id,
      @Valid @RequestBody VilleDto villeDto)
      throws ExceptionFonctionnelle {
    Ville ville = villeMapper.toEntity(villeDto);
    List<VilleDto> villesDto = villeService.modifierVille(id, ville, villeDto.codeDepartement(),
            villeDto.idDepartement()).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<List<VilleDto>> deleteVilleById(@PathVariable int id)
      throws ExceptionFonctionnelle {
    List<VilleDto> villesDto = villeService.supprimerVille(id).stream()
        .map(villeMapper::toDto)
        .toList();
    return ResponseEntity.ok(villesDto);
  }
}