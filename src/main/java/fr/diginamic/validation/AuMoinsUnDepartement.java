package fr.diginamic.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contrainte de validation au niveau classe : impose qu'au moins un des deux identifiants de
 * département (code ou id) soit renseigné. Placée sur la classe (VilleDto) plutôt que sur un champ,
 * car aucune annotation de champ ne peut exprimer une contrainte "l'un OU l'autre".
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuMoinsUnDepartementValidator.class)
public @interface AuMoinsUnDepartement {

  String message() default "Le code département ou l'identifiant département doit être renseigné";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

