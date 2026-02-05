package eap_pli24_ge3.weatherApp.events;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import eap_pli24_ge3.weatherApp.db.Crud;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;


public class ExportToPdf {
    
    public static void exportToPdf() {
        try {
            // Creating a PdfWriter
            PdfWriter writer = new PdfWriter("MostViewedCities.pdf");

            // Creating a PdfDocument
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Creating a Document
            Document document = new Document(pdfDoc);

            // Adding TITLE to the document
            Paragraph title = new Paragraph("Most Viewed Cities");
            title.setTextAlignment(TextAlignment.CENTER);
            title.setFontSize(20);
            document.add(title);

            // Adding the data to the document
            Paragraph paragraph = new Paragraph(Crud.selectCitySearches_toString());
            paragraph.setTextAlignment(TextAlignment.CENTER);
            document.add(paragraph);

            // Closing the document
            document.close();
            System.out.println("PDF Created!");
            JOptionPane.showMessageDialog(null, "PDF Created!");

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
