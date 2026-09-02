package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

  private final List<Ville> villes = new ArrayList<>();

  private void insererVille(@NonNull Ville ville) {
    //ville.creerId(); //todo : disparaîtra avec JPA — l'id sera généré par la base via @GeneratedValue
    villes.add(ville); //todo : ira dans la couche Repository (Repository.save())
  }

  //todo : ira dans la couche Repository
  public Optional<Ville> findById(int id) {
    return villes.stream()
        .filter(v -> v.getId() == id)
        .findFirst();
  }

  //todo : ira dans la couche Repository (Spring Data sait générer ça automatiquement : existsByNom)
  public boolean existsByNom(String nom) {
    return villes.stream()
        .anyMatch(v -> v.getNom().equals(nom));
  }

  //todo : deviendra un jeu de données de test (data.sql ou CommandLineRunner) plutôt qu'une méthode du contrôleur
  @PostConstruct
  public void initData() {
    insererVille(new Ville(null, "Lille", 233098));
    insererVille(new Ville(null, "Lyon", 522969));
    insererVille(new Ville(null, "Marseille", 870018));
  }

  @GetMapping
  public List<Ville> getVilles() {
    return this.villes; //todo : appellera repository.findAll()
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
        .toList(); //todo : ira dans la couche Repository (ex. findByNomStartingWithIgnoreCase)

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle( //todo : ira dans la couche Service (règle métier : que faire si aucun résultat)
          "Aucune ville dont le nom commence par " + nom + " n’a été trouvée");
    }
    return ResponseEntity.ok(resultats);
  }

  @GetMapping(value = "/recherche", params = {"min", "!max"})
  public ResponseEntity<?> getVillesByPopGreaterTo(@RequestParam int min)
      throws ExceptionFonctionnelle {
    List<Ville> resultats = villes.stream()
        .filter(v -> v.getPopulation() > min)
        .toList(); //todo : ira dans la couche Repository (ex. findByPopulationGreaterThan)

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle( //todo : ira dans la couche Service
          " Aucune ville n’a une population supérieure à " + min);
    }
    return ResponseEntity.ok(resultats);
  }

  @GetMapping(value = "/recherche", params = {"min", "max"})
  public ResponseEntity<?> getVillesByPopWithin(@RequestParam int min, @RequestParam int max)
      throws ExceptionFonctionnelle {
    List<Ville> resultats = villes.stream()
        .filter(v -> v.getPopulation() > min && v.getPopulation() < max)
        .toList(); //todo : ira dans la couche Repository (ex. findByPopulationBetween)

    if (resultats.isEmpty()) {
      throw new ExceptionFonctionnelle( //todo : ira dans la couche Service
          "Aucune ville n’a une population comprise entre " + min + " et " + max);
    }
    return ResponseEntity.ok(resultats);
  }

  @PostMapping
  public ResponseEntity<String> addVille(@Valid @RequestBody Ville ville)
  //todo : Ville (en paramètre) pourrait devenir un DTO dédié (VilleDto), pour découpler le contrat d'API du modèle de données
  {
    if (existsByNom(ville.getNom())) {
      return ResponseEntity.badRequest().body(
          "La ville existe déjà"); //todo : ira dans la couche Service (règle métier de doublon)
    }
    insererVille(ville);
    return ResponseEntity.ok("Ville insérée avec succès");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putVilleById(@PathVariable int id, @Valid @RequestBody Ville ville) {
    return findById(id)
        .<ResponseEntity<?>>map(v -> {
          v.setNom(ville.getNom());
          v.setPopulation(
              ville.getPopulation()); //todo : ira dans la couche Service (logique de mise à jour)
          return ResponseEntity.ok(v);
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("La ville n'a pas été trouvée."));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteVilleById(@PathVariable int id) {
    return findById(id)
        .<ResponseEntity<?>>map(v -> {
          villes.remove(v); //todo : ira dans la couche Repository (Repository.delete())
          return ResponseEntity.ok(Map.of("message", "Ville supprimée avec succès", "ville", v));
          //todo : ce corps de réponse pourrait devenir un DTO dédié (ex. record SuppressionResponse) au lieu d'une Map
        })
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("La ville n'a pas été trouvée."));
  }
}