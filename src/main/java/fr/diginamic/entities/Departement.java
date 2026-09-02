package fr.diginamic.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Departement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String code;
  private String nom;

  @JsonIgnore
  @OneToMany(mappedBy = "departement")
  private List<Ville> villes;

  public Departement() {
  }

  public List<Ville> getVilles() {
    return villes;
  }

  public String getNom() {
    return nom;
  }

  public String getCode() {
    return code;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setVilles(List<Ville> villes) {
    this.villes = villes;
  }
}
