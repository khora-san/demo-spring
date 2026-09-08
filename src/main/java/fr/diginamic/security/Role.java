package fr.diginamic.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * Par implémentation, Role est considérée comme une GrantendAuthority
 */
public class Role implements GrantedAuthority {

  private String name;

  public Role(String name) {
    this.name = name;
  }

  public Role() {
  }

  @Override
  public String getAuthority() {
    return name;
  }
}