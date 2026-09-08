package fr.diginamic.security;

import fr.diginamic.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

  private final UtilisateurRepository utilisateurRepository;

  public JpaUserDetailsService(UtilisateurRepository utilisateurRepository) {
    this.utilisateurRepository = utilisateurRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return utilisateurRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu : " + username));
  }
}
