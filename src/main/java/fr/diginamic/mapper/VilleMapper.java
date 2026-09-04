package fr.diginamic.mapper;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

  public VilleDto toDto(Ville ville) {
    Departement departement = ville.getDepartement();
    String code = departement != null ? departement.getCode() : null;
    Integer idDepartement = departement != null ? departement.getId() : null;
    return new VilleDto(ville.getId(), ville.getNom(), ville.getPopulation(), code, idDepartement);
  }

  public Ville toEntity(VilleDto villeDto) {
    Ville ville = new Ville();
    ville.setNom(villeDto.nom());
    ville.setPopulation(villeDto.population());
    return ville;
  }
}
