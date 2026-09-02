package fr.diginamic.controlers;

import fr.diginamic.exceptions.ExceptionFonctionnelle;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControlerAdvice {

  @ExceptionHandler({ExceptionFonctionnelle.class})
  protected ResponseEntity<String> traiterErreurs(ExceptionFonctionnelle ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> traiterException(MethodArgumentNotValidException e) {
    return ResponseEntity.badRequest().body(e.getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
        .collect(Collectors.joining(", ")));
  }
}
