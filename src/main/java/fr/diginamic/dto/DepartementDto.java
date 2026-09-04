package fr.diginamic.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartementDto(
    Integer id,
    @NotBlank(message = "Le code du département ne doit pas être vide")
    String code,

    @NotBlank(message = "Le nom du département ne doit pas être vide")
    String nom
) {

}
