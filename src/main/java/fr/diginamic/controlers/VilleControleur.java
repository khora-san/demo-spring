package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
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

  private int autoIncrementId = 1;

  @PostConstruct
  public void initData() {
    villes.add(new Ville(autoIncrementId++, "Lille", 233098));
    villes.add(new Ville(autoIncrementId++, "Lyon", 522969));
    villes.add(new Ville(autoIncrementId++, "Marseille", 870018));
  }

  @GetMapping
  public List<Ville> getVilles() {
    return this.villes;
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getVilleById(@PathVariable int id) {
    for (Ville v : villes) {
      if (v.getId() == id) {
        return ResponseEntity.ok(v);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ville n'a pas été trouvée.");
  }

  @PostMapping
  public ResponseEntity<String> addVille(@RequestBody Ville ville) {
    for (Ville v : villes) {
      if (v.getNom().equals(ville.getNom())) {
        return ResponseEntity.badRequest().body("La ville existe déjà");
      }
    }
    ville.setId(autoIncrementId);
    autoIncrementId++;
    villes.add(ville);
    return ResponseEntity.ok("Ville insérée avec succès");
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putVilleById(@PathVariable int id, @RequestBody Ville ville) {
    for (Ville v : villes) {
      if (v.getId() == id) {
        v.setNom(ville.getNom());
        v.setPopulation(ville.getPopulation());
        return ResponseEntity.ok(v);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ville n'a pas été trouvée.");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteVilleById(@PathVariable int id) {
    for (Ville v : villes) {
      if (v.getId() == id) {
        villes.remove(v);
        return ResponseEntity.ok(v);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La ville n'a pas été trouvée.");
  }
}