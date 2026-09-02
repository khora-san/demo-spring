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
import org.springframework.validation.BindingResult;

public interface VilleControleurDoc {

  @Operation(summary = "Retourne la liste de toutes les villes")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Liste des villes au format JSON",
          content = {@Content(mediaType = "application/json", array = @ArraySchema(schema =
          @Schema(implementation = Ville.class)))})
  })
  List<Ville> getVilles();

  @Operation(summary = "Affiche une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville affichée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée", content = @Content())
  })
  ResponseEntity<?> getVilleById(
      @Parameter(description = "Identifiant de la ville à afficher", example = "3", required = true) int id);

  @Operation(summary = "Ajoute une nouvelle ville")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville insérée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Données invalides ou ville déjà existante", content = @Content())
  })
  ResponseEntity<String> addVille(Ville ville, BindingResult result) throws ExceptionFonctionnelle;

  @Operation(summary = "Modifie une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville modifiée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée ou données invalides", content = @Content())
  })
  ResponseEntity<?> putVilleById(
      @Parameter(description = "Identifiant de la ville à modifier", example = "3", required = true) int id,
      Ville ville, BindingResult result)
      throws ExceptionFonctionnelle;

  @Operation(summary = "Supprime une ville existante à partir de son identifiant")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "Ville supprimée avec succès",
          content = {@Content(mediaType = "application/json")}),
      @ApiResponse(responseCode = "400",
          description = "Ville non trouvée", content = @Content())
  })
  ResponseEntity<?> deleteVilleById(
      @Parameter(description = "Identifiant de la ville à supprimer", example = "3", required = true) int id);
}

