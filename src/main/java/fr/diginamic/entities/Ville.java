package fr.diginamic.entities;

public class Ville {

  private static int autoIncrementId = 1;

  private Integer id;
  private String nom;
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
