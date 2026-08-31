package fr.diginamic.entities;

public class Ville {

  private String nom;
  private int population;

  public Ville() {
  }

  public Ville(String nom, int population) {
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
}
