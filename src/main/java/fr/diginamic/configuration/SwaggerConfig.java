package fr.diginamic.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API-Recensement-FR")
            .version("1.0")
            .description("Cette API fournit des données de recensement pour la France.")
            .contact(new Contact().name("XXX").email("xxx@maboite.fr")));
  }

}
