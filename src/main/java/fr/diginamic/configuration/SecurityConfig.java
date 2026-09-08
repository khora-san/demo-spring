package fr.diginamic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());

    // Active HTTP Basic pour les requêtes protégées
    http.httpBasic(Customizer.withDefaults());

    // Règles d'autorisation HTTP
    http.authorizeHttpRequests(auth -> auth

        // Toutes les requêtes HTTP GET sur les Villes sont accessibles sans authentification
        .requestMatchers(HttpMethod.GET, "/villes/**").permitAll()

        // Toute autre requête (POST, PUT, DELETE, GET /dpts...) nécessite une authentification
        .anyRequest().authenticated()
    );
    return http.build();
  }

}
