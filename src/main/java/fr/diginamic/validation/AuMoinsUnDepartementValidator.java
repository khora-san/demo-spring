package fr.diginamic.validation;

import fr.diginamic.dto.VilleDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implémentation de la contrainte {@link AuMoinsUnDepartement} : valide si codeDepartement() ou
 * idDepartement() est renseigné sur le VilleDto contrôlé.
 */
public class AuMoinsUnDepartementValidator implements
    ConstraintValidator<AuMoinsUnDepartement, VilleDto> {

  @Override
  public boolean isValid(VilleDto villeDto, ConstraintValidatorContext context) {
    if (villeDto == null) {
      return true;
    }
    return villeDto.codeDepartement() != null || villeDto.idDepartement() != null;
  }
}