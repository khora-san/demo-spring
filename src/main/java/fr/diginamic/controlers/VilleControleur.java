package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
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
public class VilleControleur {

  private final List<Ville> villes = new ArrayList<>();


  private void insererVille(@NonNull Ville ville) {
    ville.creerId();
    villes.add(ville);
  }

  //todo : ira dans la couche Repository
  public Optional<Ville> findById(int id) {
    return villes.stream()
        .filter(v -> v.getId() == id)
        .findFirst();
  }

  //todo : ira dans la couche Service (règle métier)
  public boolean existsByNom(String nom) {
    return villes.stream()
        .anyMatch(v -> v.getNom().equals(nom));
  }

  public void verifierVille(Ville ville) throws ExceptionFonctionnelle {
    if (ville.getPopulation() < 10) {
      throw new ExceptionFonctionnelle("La population doit être supérieure ou égale à 10");
    }
    if (ville.getNom().length() < 2) {
      throw new ExceptionFonctionnelle("Le nom doit contenir au moins 2 lettres");
    }
  }

  @PostConstruct
  public void initData() {
    insererVille(new Ville(null, "Lille", 233098));
    insererVille(new Ville(null, "Lyon", 522969));
    insererVille(new Ville(null, "Marseille", 870018));
  }

  @GetMapping
  public List<Ville> getVilles() {
    return this.villes;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getVilleById(@PathVariable int id) {
    return findById(id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("La ville n'a pas été trouvée."));
  }

  @GetMapping(value = "/recherche", params = "nom")
  public ResponseEntity<?> getVillesByNameStartWith(@RequestParam String nom)
      throws ExceptionFonctionnelle {
    List<Ville> resultats = villes.stream()
        .filter(v -> v.getNom().toUpperCase().startsWith(nom.toUpperCase()))
        .toList();

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle(
          "Aucune ville dont le nom commence par " + nom + " n’a été trouvée");
    }
    return ResponseEntity.ok(resultats);
  }

  @GetMapping(value = "/recherche", params = {"min", "!max"})
  public ResponseEntity<?> getVillesByPopGreaterTo(@RequestParam int min)
      throws ExceptionFonctionnelle {
    List<Ville> resultats = villes.stream()
        .filter(v -> v.getPopulation() > min)
        .toList();

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle(
          " Aucune ville n’a une population supérieure à " + min);
    }
    return ResponseEntity.ok(resultats);
  }

  @GetMapping(value = "/recherche", params = {"min", "max"})
  public ResponseEntity<?> getVillesByPopWithin(@RequestParam int min, @RequestParam int max)
      throws ExceptionFonctionnelle {
    List<Ville> resultats = villes.stream()
        .filter(v -> v.getPopulation() > min && v.getPopulation() < max)
        .toList();

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle(
          "Aucune ville n’a une population comprise entre " + min + " et " + max);
    }
    return ResponseEntity.ok(resultats);
  }


  @PostMapping
  public ResponseEntity<String> addVille(@RequestBody Ville ville) throws ExceptionFonctionnelle {
    if (existsByNom(ville.getNom())) {
      return ResponseEntity.badRequest().body("La ville existe déjà");
    }
    verifierVille(ville);
    insererVille(ville);

    return ResponseEntity.ok("Ville insérée avec succès");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putVilleById(@PathVariable int id, @RequestBody Ville ville)
      throws ExceptionFonctionnelle {
    verifierVille(ville);
    return findById(id)
        .<ResponseEntity<?>>map(v -> {
          v.setNom(ville.getNom());
          v.setPopulation(ville.getPopulation());
          return ResponseEntity.ok(v);
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("La ville n'a pas été trouvée."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteVilleById(@PathVariable int id) {
    return findById(id)
        .<ResponseEntity<?>>map(v -> {
          villes.remove(v);
          return ResponseEntity.ok(Map.of("message", "Ville supprimée avec succès", "ville", v));
          //Map.of pour combiner code 200 body+text
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("La ville n'a pas été trouvée."));
  }
}