package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
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

  @PostMapping
  public ResponseEntity<String> addVille(@RequestBody Ville ville) {
    if (existsByNom(ville.getNom())) {
      return ResponseEntity.badRequest().body("La ville existe déjà");
    }
    insererVille(ville);
    return ResponseEntity.ok("Ville insérée avec succès");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putVilleById(@PathVariable int id, @RequestBody Ville ville) {
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