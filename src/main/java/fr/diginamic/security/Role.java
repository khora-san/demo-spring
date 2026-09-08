package fr.diginamic.security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.security.core.GrantedAuthority;

/**
 * Par implémentation, Role est considérée comme une GrantedAuthority
 */
@Entity
public class Role implements GrantedAuthority {

  private String name;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "id_utilisateur")
  private Utilisateur utilisateur;

  public Role(String name) {
    this.name = name;
  }

  public Role() {
  }

  @Override
  public String getAuthority() {
    return name;
  }

  public void setUtilisateur(Utilisateur utilisateur) {
    this.utilisateur = utilisateur;
  }
}