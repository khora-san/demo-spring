package fr.diginamic.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Ville {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotBlank(message = "Le nom de la ville ne doit pas être vide")
  @Size(min = 2, message = "Le nom de la ville doit posséder au moins 2 caractères")
  private String nom;

  @Min(value = 1, message = "Le nombre d'habitants ne peut pas être zéro")
  @Column(name = "nb_habs")
  private int population;

  @ManyToOne
  @JoinColumn(name = "id_dept")
  @NotNull(message = "Le département de la ville doit être renseigné")
  private Departement departement;


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

  public Integer getId() {
    return id;
  }

  public Departement getDepartement() {
    return departement;
  }

  public void setDepartement(Departement departement) {
    this.departement = departement;
  }
}
