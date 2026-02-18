package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.AppLogo;
import gr.eap.weatherapp.main.Forecast;
import gr.eap.weatherapp.rest.*;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;


public class SearchOptions extends javax.swing.JFrame {

    // Δηλώσεις μεταβλητών
    private javax.swing.ButtonGroup searchTypeGroup;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInputPrompt;
    private javax.swing.JTextField txtSearchInput; 
    private javax.swing.JRadioButton rbCity;
    private javax.swing.JRadioButton rbLocation;
    private javax.swing.JRadioButton rbAreaCode;
    private javax.swing.JRadioButton rbCoordinates;
    private javax.swing.JRadioButton rbAirportCode;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnBack; 

    public SearchOptions() {
        initComponents();
        setIconImage(AppLogo.setIconImage());
    }

    private void initComponents() {

        setTitle("Search Weather Data");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // Τα κενά μεταξύ των στοιχείων
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Τίτλος
        lblHeader = new JLabel("ΕΠΙΛΕΞΕ ΤΡΟΠΟ ΑΝΑΖΗΤΗΣΗΣ ΚΑΙΡΟΥ");
        lblHeader.setFont(new Font("Segoe UI", 1, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Καταλαμβάνει 2 στήλες
        add(lblHeader, gbc);

        // 2. ButtonGroup & Radio Buttons
        searchTypeGroup = new ButtonGroup();
        rbCity = new JRadioButton("Πόλη", true);
        rbLocation = new JRadioButton("Τοποθεσία");
        rbAreaCode = new JRadioButton("Κωδικός Περιοχής");
        rbCoordinates = new JRadioButton("Συντεταγμένες");
        rbAirportCode = new JRadioButton("Κωδικός Αεροδρομίου");

        searchTypeGroup.add(rbCity);
        searchTypeGroup.add(rbLocation);
        searchTypeGroup.add(rbAreaCode);
        searchTypeGroup.add(rbCoordinates);
        searchTypeGroup.add(rbAirportCode);

        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 1; add(rbCity, gbc);
        gbc.gridy = 2; add(rbLocation, gbc);
        gbc.gridy = 3; add(rbAreaCode, gbc);
        gbc.gridy = 4; add(rbCoordinates, gbc);
        gbc.gridy = 5; add(rbAirportCode, gbc);

        // 3. Input
        lblInputPrompt = new JLabel("Στοιχείο προς αναζήτηση:");
        gbc.gridy = 6; add(lblInputPrompt, gbc);
        txtSearchInput = new JTextField(20);
        gbc.gridy = 7; add(txtSearchInput, gbc);

        // 4. Κουμπί Αναζήτησης (btnSearch)
        btnSearch = new JButton("Αναζήτηση");
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1; // Επιστροφή σε 1 στήλη
        add(btnSearch, gbc);

        // 5. Κουμπί Επιστροφής (btnBack)
        btnBack = new JButton("Επιστροφή");
        btnBack.addActionListener(evt -> this.dispose());
        gbc.gridx = 1; gbc.gridy = 8;
        add(btnBack, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        // Έλεγχος αν είναι επιλεγμένη η αναζήτηση ανά πόλη
        if (!rbCity.isSelected()) {
            JOptionPane.showMessageDialog(this, "Η επιλογή αυτή θα είναι διαθέσιμη σε μελλοντική έκδοση.");
            return;
        }

        String city = txtSearchInput.getText().trim(); 
        if (city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε όνομα πόλης.");
            return;
        }
        // Απενεργοποίηση του κουμπιού όσο δουλεύει το background thread
        btnSearch.setEnabled(false);
        btnSearch.setText("Αναζήτηση...");

        // Χρήση της SwingWorker για να μην παγώσει το Main Menu
        SwingWorker<ArrayList<ArrayList<Forecast>>, Void> worker = new SwingWorker<>() {
            @Override
            protected ArrayList<ArrayList<Forecast>> doInBackground() throws Exception {
                // 1. Κλήση του API (Στο παρασκήνιο)
                String jsonResponse = HttpCall.callAPI(UrlBuilder.buildUrl(city));
                
                if (jsonResponse == null || !jsonResponse.trim().startsWith("{")) {
                    throw new Exception("Η πόλη δεν βρέθηκε ή το API δεν αποκρίνεται.");
                }

                WeatherDataParser parser = new WeatherDataParser();
                ArrayList<ArrayList<Forecast>> data = parser.parseWeatherData(jsonResponse);

            // 2. Ενημέρωση Βάσης Δεδομένων (Στο παρασκήνιο)
            try {
                Crud.createTableCitySearches();
                Crud.insertDataToCitySearches(data.get(0).get(0).getCity());
            } catch (Exception dbEx) {
                System.err.println("Database non-critical error: " + dbEx.getMessage());
                // Δεν σταματάει η εφαρμογή αν αποτύχει μόνο η καταγραφή των στατιστικών
            }
            
            return data;
        }        
        @Override
        protected void done() {
            try {
                // Επιστροφή στο κεντρικό νήμα για την εμφάνιση του παραθύρου
                ArrayList<ArrayList<Forecast>> weatherData = get();
                new ForecastDisplay(weatherData).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(SearchOptions.this, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
            } finally {
                // Επαναφορά του GUI
                btnSearch.setEnabled(true);
                btnSearch.setText("Αναζήτηση");
            }
        }
    };
    worker.execute();
    }
}