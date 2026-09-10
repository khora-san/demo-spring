package fr.diginamic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitaire de lecture/écriture de propriétés dans un fichier .properties.
 */
public final class PropertiesFileUtils {

  private static final Logger log = LoggerFactory.getLogger(PropertiesFileUtils.class);

  private PropertiesFileUtils() {
    // classe utilitaire, non instanciable
  }

  /**
   * Met à jour une propriété dans un fichier .properties existant.
   *
   * @param cheminFichier chemin du fichier à modifier
   * @param cle           clé de la propriété à modifier
   * @param valeur        nouvelle valeur de la propriété
   */
  public static void updateProperty(String cheminFichier, String cle, String valeur) {
    File fichier = new File(cheminFichier);
    Properties properties = new Properties();

    try (FileInputStream in = new FileInputStream(fichier)) {
      properties.load(in);
    } catch (IOException e) {
      log.warn("Impossible de lire {} pour mettre à jour la propriété {}", cheminFichier, cle, e);
      return;
    }

    properties.setProperty(cle, valeur);

    try (FileOutputStream out = new FileOutputStream(fichier)) {
      properties.store(out, null);
    } catch (IOException e) {
      log.warn("Impossible d'écrire {} pour mettre à jour la propriété {}", cheminFichier, cle, e);
    }
  }
}