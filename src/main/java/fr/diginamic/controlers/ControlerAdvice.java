package fr.diginamic.controlers;

import fr.diginamic.exceptions.ExceptionFonctionnelle;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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
  public ResponseEntity<String> traiterException(MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().stream()
        .map(error -> {
          if (error instanceof FieldError fieldError) {
            return fieldError.getField() + " : " + fieldError.getDefaultMessage();
          }
          return error.getDefaultMessage(); // erreur globale, comme AuMoinsUnDepartement
        })
        .collect(Collectors.joining(", "));
    return ResponseEntity.badRequest().body(message);
  }
}
