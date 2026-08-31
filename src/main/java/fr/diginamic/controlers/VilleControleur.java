package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

  private final List<Ville> villes = new ArrayList<>();

  @GetMapping
  public List<Ville> getVilles() {
    return this.villes;
  }

  @PostConstruct
  public void initData() {
    villes.add(new Ville("Lille", 233098));
    villes.add(new Ville("Lyon", 522969));
    villes.add(new Ville("Marseille", 870018));
  }

  @PostMapping
  public ResponseEntity<String> addVille(@RequestBody Ville ville) {
    for (Ville v : villes) {
      if (v.getNom().equals(ville.getNom())) {
        return ResponseEntity.badRequest().body("La ville existe déjà");
      }
    }
    villes.add(ville);
    return ResponseEntity.ok("Ville insérée avec succès");
  }
//  // Alternative avec un Stream
//  @PostMapping
//  public ResponseEntity<String> addVille(@RequestBody Ville ville) {
//    boolean existeDeja = villes.stream()
//        .anyMatch(v -> v.getNom().equals(ville.getNom()));
//
//    if (existeDeja) {
//      return ResponseEntity.badRequest().body("La ville existe déjà");
//    }
//
//    villes.add(ville);
//    return ResponseEntity.ok("Ville insérée avec succès");
//  }
}