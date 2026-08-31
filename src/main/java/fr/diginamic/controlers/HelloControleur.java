package fr.diginamic.controlers;

import fr.diginamic.services.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloControleur {

  private final HelloService helloService;

  public HelloControleur(HelloService helloService) {
    this.helloService = helloService;
  }

  @GetMapping
  public String direHello() {
    return helloService.salutations();
  }
}