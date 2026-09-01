package fr.diginamic.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Ville {

  private static int autoIncrementId = 1;

  private Integer id;
  @NotBlank(message = "Le nom de la ville ne doit pas être vide")
  @Size(min = 2, message = "Le nom de la ville doit posséder au moins 2 caractères")
  private String nom;

  @Min(value = 1, message = "Le nombre d'habitants ne peut pas être zéro")
  private int population;


  public Ville() {
  }

  public Ville(Integer id, String nom, int population) {
    this.id = id;
    this.nom = nom;
    this.population = population;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public void creerId() {
    this.id = autoIncrementId++;
  }

  public Integer getId() {
    return id;
  }

}
