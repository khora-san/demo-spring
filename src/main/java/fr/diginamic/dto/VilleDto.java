package fr.diginamic.dto;

import fr.diginamic.validation.AuMoinsUnDepartement;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@AuMoinsUnDepartement
public record VilleDto(
    Integer id,

    @NotBlank(message = "Le nom de la ville ne doit pas être vide")
    @Size(min = 2, message = "Le nom de la ville doit posséder au moins 2 caractères")
    String nom,

    @Min(value = 1, message = "Le nombre d'habitants ne peut pas être zéro")
    int population,

    String codeDepartement,
    Integer idDepartement

) {

}