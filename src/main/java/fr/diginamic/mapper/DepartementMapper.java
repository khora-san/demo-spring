package fr.diginamic.mapper;

import fr.diginamic.dto.DepartementDto;
import fr.diginamic.entities.Departement;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

  public DepartementDto toDto(Departement departement) {
    return new DepartementDto(departement.getId(), departement.getCode(), departement.getNom());
  }

  public Departement toEntity(DepartementDto departementDto) {
    Departement departement = new Departement();
    departement.setCode(departementDto.code());
    departement.setNom(departementDto.nom());
    return departement;
  }

}
