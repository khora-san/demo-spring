package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

  @GetMapping
  public List<Ville> getVilles() {
    return List.of(
        new Ville("Lille", 233098),
        new Ville("Lyon", 522969),
        new Ville("Marseille", 870018)
    );
  }
}