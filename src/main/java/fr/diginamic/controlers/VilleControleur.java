package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
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

  public VilleControleur(VilleService villeService) {
    this.villeService = villeService;
  }

  @Override
  @GetMapping
  public ResponseEntity<List<Ville>> getVilles() {
    return ResponseEntity.ok(villeService.extractVilles());
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<Ville> getVilleById(@PathVariable int id) throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.extractVille(id));
  }

  @Override
  @GetMapping(value = "/recherche", params = "prefixe")
  public ResponseEntity<List<Ville>> getVillesByNameStartWith(@RequestParam String prefixe)
      throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.extractVillesByNameStartWith(prefixe));
  }

  @Override
  @GetMapping(value = "/recherche", params = {"min", "!max"})
  public ResponseEntity<List<Ville>> getVillesByPopGreaterTo(@RequestParam int min)
      throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.extractVillesByPopulationSuperieure(min));
  }

  @Override
  @GetMapping(value = "/recherche", params = {"min", "max"})
  public ResponseEntity<List<Ville>> getVillesByPopWithin(@RequestParam int min,
      @RequestParam int max)
      throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.extractVillesByPopulationEntre(min, max));
  }

  @Override
  @PostMapping
  public ResponseEntity<List<Ville>> addVille(@Valid @RequestBody Ville ville)
      throws ExceptionFonctionnelle
  //todo : Ville (en paramètre) pourrait devenir un DTO dédié (VilleDto), pour découpler le contrat d'API du modèle de données
  {
    return ResponseEntity.ok(villeService.insertVille(ville));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<List<Ville>> putVilleById(@PathVariable int id,
      @Valid @RequestBody Ville ville)
      throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.modifierVille(id, ville));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<List<Ville>> deleteVilleById(@PathVariable int id)
      throws ExceptionFonctionnelle {
    return ResponseEntity.ok(villeService.supprimerVille(id));
  }
}