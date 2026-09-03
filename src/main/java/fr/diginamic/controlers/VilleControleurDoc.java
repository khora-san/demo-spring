package fr.diginamic.controlers;

import fr.diginamic.entities.Ville;
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
          @Schema(implementation = Ville.class)))})
  })
  ResponseEntity<List<Ville>> getVilles();

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
  ResponseEntity<Ville> getVilleById(
      @Parameter(description = "Identifiant de la ville à afficher", example = "3", required = true) int id)
      throws ExceptionFonctionnelle;

  /**
   * Ajoute une nouvelle ville en base.
   *
   * @param ville les données de la ville à créer
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
  ResponseEntity<List<Ville>> addVille(Ville ville) throws ExceptionFonctionnelle;

  /**
   * Modifie les données d'une ville existante.
   *
   * @param id    l'identifiant de la ville à modifier
   * @param ville les nouvelles données à appliquer
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
  ResponseEntity<List<Ville>> putVilleById(
      @Parameter(description = "Identifiant de la ville à modifier", example = "3", required = true) int id,
      Ville ville) throws ExceptionFonctionnelle;

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
  ResponseEntity<List<Ville>> deleteVilleById(
      @Parameter(description = "Identifiant de la ville à supprimer", example = "3", required = true) int id)
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
          @Schema(implementation = Ville.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne correspond au préfixe", content = @Content())
  })
  ResponseEntity<List<Ville>> getVillesByNameStartWith(
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
          @Schema(implementation = Ville.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne dépasse ce seuil de population", content = @Content())
  })
  ResponseEntity<List<Ville>> getVillesByPopGreaterTo(
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
          @Schema(implementation = Ville.class)))}),
      @ApiResponse(responseCode = "400",
          description = "Aucune ville ne correspond à cet intervalle de population", content = @Content())
  })
  ResponseEntity<List<Ville>> getVillesByPopWithin(
      @Parameter(description = "Population minimum", example = "50000", required = true) int min,
      @Parameter(description = "Population maximum", example = "200000", required = true) int max)
      throws ExceptionFonctionnelle;
}