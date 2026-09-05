package fr.diginamic.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import fr.diginamic.entities.Departement;
import fr.diginamic.entities.Ville;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Construit le document PDF de présentation d'un département : en-tête, bandeau résumé et liste des
 * villes réparties sur deux colonnes avec pagination automatique.
 */
public final class DepartementPdfExporter {

  private static final float ROW_HEIGHT = 20f;
  private static final float HEADER_HEIGHT = 26f;

  private static final BaseColor ACCENT = new BaseColor(41, 65, 92);
  private static final BaseColor GRIS_CLAIR = new BaseColor(245, 245, 245);

  private DepartementPdfExporter() {
    // classe utilitaire, non instanciable
  }

  /**
   * Génère le PDF de présentation d'un département et l'écrit dans le flux fourni.
   *
   * @param departement  département à présenter
   * @param villes       villes de ce département, triées par population décroissante
   * @param outputStream flux de sortie dans lequel écrire le document
   * @throws DocumentException en cas d'erreur de construction du document
   */
  public static void export(Departement departement, List<Ville> villes, OutputStream outputStream)
      throws DocumentException {
    int nombreVilles = villes.size();
    long populationTotale = villes.stream().mapToLong(Ville::getPopulation).sum();
    NumberFormat formatNombre = NumberFormat.getIntegerInstance(Locale.FRANCE);

    Font titreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, ACCENT);
    Font sousTitreFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, ACCENT);
    Font labelFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.GRAY);
    Font valeurFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
    Font enteteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    Font villeFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
    Font popFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, ACCENT);

    Document document = new Document(PageSize.A4, 40, 40, 50, 50);
    try {
      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
      document.open();

      Paragraph titre = new Paragraph("Département " + departement.getCode(), titreFont);
      titre.setSpacingAfter(20);
      document.add(titre);

      PdfPTable bandeau = new PdfPTable(3);
      bandeau.setWidthPercentage(100);
      bandeau.setSpacingAfter(30);
      bandeau.addCell(celluleBandeau("CODE", departement.getCode(), labelFont, valeurFont));
      bandeau.addCell(
          celluleBandeau("NOMBRE DE VILLES", String.valueOf(nombreVilles), labelFont, valeurFont));
      bandeau.addCell(
          celluleBandeau("POPULATION TOTALE", formatNombre.format(populationTotale), labelFont,
              valeurFont));
      document.add(bandeau);

      Paragraph sousTitre = new Paragraph("Villes du département", sousTitreFont);
      sousTitre.setSpacingAfter(10);
      document.add(sousTitre);

      float hauteurPageComplete =
          document.getPageSize().getHeight() - document.topMargin() - document.bottomMargin();
      float positionY = writer.getVerticalPosition(true);
      float hauteurDisponiblePage1 = positionY - document.bottomMargin();
      int capacitePage1 =
          ((int) Math.floor((hauteurDisponiblePage1 - HEADER_HEIGHT) / ROW_HEIGHT)) * 2;
      int capacitePageSuivante =
          ((int) Math.floor((hauteurPageComplete - HEADER_HEIGHT) / ROW_HEIGHT)) * 2;

      int index = 0;
      boolean premierePage = true;
      while (index < nombreVilles) {
        int capacite = premierePage ? capacitePage1 : capacitePageSuivante;
        int finChunk = Math.min(index + capacite, nombreVilles);
        List<Ville> chunk = villes.subList(index, finChunk);
        document.add(construireTableVilles(chunk, enteteFont, villeFont, popFont, formatNombre));
        index = finChunk;
        premierePage = false;
        if (index < nombreVilles) {
          document.newPage();
        }
      }
    } finally {
      document.close();
    }
  }

  private static PdfPTable construireTableVilles(List<Ville> chunk, Font enteteFont, Font villeFont,
      Font popFont, NumberFormat formatNombre) throws DocumentException {
    int total = chunk.size();
    int nombreLignes = (int) Math.ceil(total / 2.0);

    PdfPTable table = new PdfPTable(5);
    table.setWidthPercentage(100);
    table.setWidths(new float[]{3, 1.6f, 0.5f, 3, 1.6f});

    table.addCell(enteteCellule("Ville", enteteFont, Element.ALIGN_LEFT));
    table.addCell(enteteCellule("Pop.", enteteFont, Element.ALIGN_RIGHT));
    table.addCell(celluleEspacement());
    table.addCell(enteteCellule("Ville", enteteFont, Element.ALIGN_LEFT));
    table.addCell(enteteCellule("Pop.", enteteFont, Element.ALIGN_RIGHT));

    for (int ligne = 0; ligne < nombreLignes; ligne++) {
      BaseColor fond = (ligne % 2 == 0) ? BaseColor.WHITE : GRIS_CLAIR;

      int indexGauche = ligne;
      ajouterVille(table, indexGauche < total ? chunk.get(indexGauche) : null,
          villeFont, popFont, fond, formatNombre);

      table.addCell(celluleEspacement());

      int indexDroite = ligne + nombreLignes;
      ajouterVille(table, indexDroite < total ? chunk.get(indexDroite) : null,
          villeFont, popFont, fond, formatNombre);
    }
    return table;
  }

  private static void ajouterVille(PdfPTable table, Ville ville, Font villeFont, Font popFont,
      BaseColor fond, NumberFormat formatNombre) {
    if (ville != null) {
      table.addCell(donneeCellule(ville.getNom(), villeFont, fond, Element.ALIGN_LEFT));
      table.addCell(donneeCellule(formatNombre.format(ville.getPopulation()), popFont, fond,
          Element.ALIGN_RIGHT));
    } else {
      table.addCell(donneeCellule("", villeFont, fond, Element.ALIGN_LEFT));
      table.addCell(donneeCellule("", popFont, fond, Element.ALIGN_RIGHT));
    }
  }

  private static PdfPCell celluleBandeau(String label, String valeur, Font labelFont,
      Font valeurFont) {
    PdfPCell cellule = new PdfPCell();
    cellule.setBackgroundColor(GRIS_CLAIR);
    cellule.setBorder(PdfPCell.NO_BORDER);
    cellule.setPadding(12);
    cellule.setHorizontalAlignment(Element.ALIGN_CENTER);
    cellule.addElement(new Paragraph(label, labelFont));
    cellule.addElement(new Paragraph(valeur, valeurFont));
    return cellule;
  }

  private static PdfPCell enteteCellule(String texte, Font font, int alignement) {
    PdfPCell cellule = new PdfPCell(new Phrase(texte, font));
    cellule.setBackgroundColor(ACCENT);
    cellule.setPaddingTop(8);
    cellule.setPaddingBottom(8);
    cellule.setPaddingLeft(4);
    cellule.setPaddingRight(4);
    cellule.setHorizontalAlignment(alignement);
    cellule.setBorder(PdfPCell.NO_BORDER);
    cellule.setNoWrap(true);
    return cellule;
  }

  private static PdfPCell donneeCellule(String texte, Font font, BaseColor fond, int alignement) {
    PdfPCell cellule = new PdfPCell(new Phrase(texte, font));
    cellule.setBackgroundColor(fond);
    cellule.setFixedHeight(ROW_HEIGHT);
    cellule.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cellule.setPaddingLeft(3);
    cellule.setPaddingRight(3);
    cellule.setHorizontalAlignment(alignement);
    cellule.setBorder(PdfPCell.NO_BORDER);
    cellule.setNoWrap(true);
    return cellule;
  }

  private static PdfPCell celluleEspacement() {
    PdfPCell cellule = new PdfPCell();
    cellule.setBorder(PdfPCell.NO_BORDER);
    return cellule;
  }
}
