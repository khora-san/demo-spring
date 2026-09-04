package fr.diginamic.controlers;

import fr.diginamic.dto.DepartementDto;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Contrat du contrôleur REST pour la gestion des départements, séparé de son implémentation
 * ({@code DepartementControleur}) afin de ne pas surcharger celle-ci avec les annotations de
 * documentation Swagger/OpenAPI.
 */
public interface DepartementControleurDoc {

  /**
   * Retourne l'ensemble des départements.
   *
   * @return la liste de tous les départements
   */
  @Operation(summary = "Retourne la liste de tous les départements")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des départements au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = DepartementDto.class)))})
  })
  ResponseEntity<List<DepartementDto>> getDepartements();

  /**
   * Retourne le département correspondant à l'identifiant donné.
   *
   * @param id l'identifiant du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne correspond à cet identifiant
   */
  @Operation(summary = "Affiche un département existant à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Département affiché avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé", content = @Content())
  })
  ResponseEntity<DepartementDto> getDepartementById(
      @Parameter(description = "Id du département à afficher", example = "3", required = true) int id)
      throws ExceptionFonctionnelle;

  /**
   * Retourne le département correspondant au code donné.
   *
   * @param code le code du département recherché
   * @return le département correspondant
   * @throws ExceptionFonctionnelle si aucun département ne correspond à ce code
   */
  @Operation(summary = "Affiche un département existant à partir de son code")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Département affiché avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé", content = @Content())
  })
  ResponseEntity<DepartementDto> getDepartementByCode(
      @Parameter(description = "Code du département à afficher", example = "34", required = true) String code)
      throws ExceptionFonctionnelle;

  /**
   * Ajoute un nouveau département en base.
   *
   * @param departementDto les données du département à créer
   * @return le département créé, avec son identifiant généré
   * @throws ExceptionFonctionnelle si les données sont invalides ou si un département du même code
   *                                existe déjà
   */
  @Operation(summary = "Ajoute un nouveau département")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Département inséré avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Données invalides ou département déjà existant", content = @Content())
  })
  ResponseEntity<DepartementDto> createDepartement(DepartementDto departementDto)
      throws ExceptionFonctionnelle;

  /**
   * Modifie les données d'un département existant.
   *
   * @param id             l'identifiant du département à modifier
   * @param departementDto les nouvelles données à appliquer
   * @return le département modifié
   * @throws ExceptionFonctionnelle si le département n'existe pas ou si les données sont invalides
   */
  @Operation(summary = "Modifie un département existant à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Département modifié avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé ou données invalides", content = @Content())
  })
  ResponseEntity<DepartementDto> updateDepartementById(
      @Parameter(description = "Id du département à modifier", example = "3", required = true) int id,
      DepartementDto departementDto)
      throws ExceptionFonctionnelle;

  /**
   * Supprime un département existant.
   *
   * @param id l'identifiant du département à supprimer
   * @throws ExceptionFonctionnelle si aucun département ne correspond à cet identifiant
   */
  @Operation(summary = "Supprime un département existant à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Département supprimé avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé", content = @Content())
  })
  ResponseEntity<?> deleteDepartementById(
      @Parameter(description = "Id du département à supprimer", example = "3", required = true) int id)
      throws ExceptionFonctionnelle;

}