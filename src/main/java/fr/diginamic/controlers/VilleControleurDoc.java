package fr.diginamic.controlers;

import fr.diginamic.dto.VilleDto;
import fr.diginamic.exceptions.ExceptionFonctionnelle;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * Contrat du contrôleur REST pour la gestion des villes, séparé de son implémentation
 * ({@code VilleControleur}) afin de ne pas surcharger celle-ci avec les annotations de
 * documentation Swagger/OpenAPI.
 */
public interface VilleControleurDoc {

  /**
   * Retourne la liste de toutes les villes présentes en base.
   *
   * @return la liste des villes, au format JSON
   */
  @Operation(summary = "Retourne la liste de toutes les villes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))})
  })
  ResponseEntity<Page<VilleDto>> getVilles(int page, int size);

  /**
   * Retourne la ville correspondant à l'identifiant donné.
   *
   * @param id l'identifiant de la ville recherchée
   * @return la ville correspondante
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  @Operation(summary = "Affiche une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville affichée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée", content = @Content())
  })
  ResponseEntity<VilleDto> getVilleById(
      @Parameter(description = "Identifiant de la ville à afficher", example = "3", required = true) int id)
      throws ExceptionFonctionnelle;

  /**
   * Retourne les villes dont le nom commence par le préfixe donné.
   *
   * @param prefixe le préfixe recherché dans le nom des villes
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne correspond au préfixe
   */
  @Operation(summary = "Retourne les villes dont le nom commence par un préfixe donné")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes correspondantes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne correspond au préfixe", content = @Content())
  })
  ResponseEntity<List<VilleDto>> getVillesByNameStartWith(
      @Parameter(description = "Préfixe recherché dans le nom des villes", example = "Li", required = true) String prefixe)
      throws ExceptionFonctionnelle;

  /**
   * Retourne les villes dont la population est strictement supérieure au minimum donné.
   *
   * @param min le seuil minimum de population (exclu)
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne dépasse ce seuil
   */
  @Operation(summary = "Retourne les villes dont la population est supérieure à un minimum donné")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes correspondantes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne dépasse ce seuil de population", content = @Content())
  })
  ResponseEntity<List<VilleDto>> getVillesByPopGreaterTo(
      @Parameter(description = "Population minimum (exclue)", example = "100000", required = true) int min)
      throws ExceptionFonctionnelle;

  /**
   * Retourne les villes dont la population est comprise entre les bornes données.
   *
   * @param min la population minimum
   * @param max la population maximum
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet intervalle
   */
  @Operation(summary = "Retourne les villes dont la population est comprise entre deux bornes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes correspondantes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne correspond à cet intervalle de population", content = @Content())
  })
  ResponseEntity<List<VilleDto>> getVillesByPopWithin(
      @Parameter(description = "Population minimum", example = "50000", required = true) int min,
      @Parameter(description = "Population maximum", example = "200000", required = true) int max)
      throws ExceptionFonctionnelle;

  /**
   * Retourne les n villes les plus peuplées d'un département donné.
   *
   * @param code le code du département concerné
   * @param n    le nombre de villes à retourner
   * @return la liste des n villes les plus peuplées du département
   * @throws ExceptionFonctionnelle si le code de département est invalide, ou si aucune ville n'est
   *                                trouvée pour ce département
   */
  @Operation(summary = "Retourne les n villes les plus peuplées d'un département")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé ou aucune ville trouvée", content = @Content())
  })
  ResponseEntity<List<VilleDto>> getTopVillesByDepartementCode(
      @Parameter(description = "Code du département concerné", example = "34", required = true) String code,
      @Parameter(description = "Nombre de villes à retourner", example = "5", required = true) int n)
      throws ExceptionFonctionnelle;

  /**
   * Retourne les villes d'un département donné dont la population est comprise entre deux bornes.
   *
   * @param code le code du département concerné
   * @param min  la population minimale
   * @param max  la population maximale
   * @return la liste des villes du département correspondant aux critères
   * @throws ExceptionFonctionnelle si le code de département est invalide, ou si aucune ville n'est
   *                                trouvée pour ces critères
   */
  @Operation(summary = "Retourne les villes d'un département dont la population est comprise entre deux bornes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = VilleDto.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Département non trouvé ou aucune ville trouvée", content = @Content())
  })
  ResponseEntity<List<VilleDto>> getVillesByPopulationEntreAndDepartementCode(
      @Parameter(description = "Code du département concerné", example = "34", required = true) String code,
      @Parameter(description = "Population minimale", example = "1000", required = true) int min,
      @Parameter(description = "Population maximale", example = "50000", required = true) int max)
      throws ExceptionFonctionnelle;

  /**
   * Recherche les villes d'un département dont la population est strictement supérieure à un seuil
   * donné, triées par population décroissante.
   *
   * @param code code du département concerné
   * @param min  population minimale (strictement)
   * @return la liste des villes correspondantes
   * @throws ExceptionFonctionnelle si le département n'existe pas ou si aucune ville ne correspond
   */
  @Operation(summary = "Rechercher les villes d'un département par population minimale",
      description = "Retourne les villes du département dont la population est strictement "
          + "supérieure au seuil donné, triées par population décroissante.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Liste des villes correspondantes"),
      @ApiResponse(responseCode = "404",
          description = "Département inconnu ou aucune ville correspondante")
  })
  ResponseEntity<List<VilleDto>> getVillesByPopulationSuperieureAndDepartementCode(
      @Parameter(description = "Code du département concerné") String code,
      @Parameter(description = "Population minimale (strictement)") int min)
      throws ExceptionFonctionnelle;

  /**
   * Ajoute une nouvelle ville en base.
   *
   * @param villeDto les données de la ville à créer
   * @return la liste des villes après insertion
   * @throws ExceptionFonctionnelle si les données sont invalides ou si une ville du même nom existe
   *                                déjà
   */
  @Operation(summary = "Ajoute une nouvelle ville")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville insérée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Données invalides ou ville déjà existante", content = @Content())
  })
  ResponseEntity<List<VilleDto>> addVille(VilleDto villeDto) throws ExceptionFonctionnelle;

  /**
   * Modifie les données d'une ville existante.
   *
   * @param id       l'identifiant de la ville à modifier
   * @param villeDto les nouvelles données à appliquer
   * @return la liste des villes après modification
   * @throws ExceptionFonctionnelle si la ville n'existe pas ou si les données sont invalides
   */
  @Operation(summary = "Modifie une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville modifiée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée ou données invalides", content = @Content())
  })
  ResponseEntity<List<VilleDto>> putVilleById(
      @Parameter(description = "Identifiant de la ville à modifier", example = "3", required = true) int id,
      VilleDto villeDto) throws ExceptionFonctionnelle;

  /**
   * Supprime une ville existante.
   *
   * @param id l'identifiant de la ville à supprimer
   * @return la liste des villes après suppression
   * @throws ExceptionFonctionnelle si aucune ville ne correspond à cet identifiant
   */
  @Operation(summary = "Supprime une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville supprimée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée", content = @Content())
  })
  ResponseEntity<List<VilleDto>> deleteVilleById(
      @Parameter(description = "Identifiant de la ville à supprimer", example = "3", required = true) int id)
      throws ExceptionFonctionnelle;


}