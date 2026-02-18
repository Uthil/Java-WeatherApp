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
        if (rbCity.isSelected()) {
            String city = txtSearchInput.getText().trim();
            
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ εισάγετε όνομα πόλης.");
                return;
            }

            try {
                // Ανάκτηση Δεδομένων
                String jsonResponse = HttpCall.callAPI(UrlBuilder.buildUrl(city));
                
                // Έλεγχος αν το API επέστρεψε έγκυρο JSON
                if (jsonResponse!= null && jsonResponse.startsWith("{")) {
                    WeatherDataParser parser = new WeatherDataParser();
                    ArrayList<ArrayList<Forecast>> weatherData = parser.parseWeatherData(jsonResponse);

                    new ForecastDisplay(weatherData).setVisible(true);

                    // Ενημέρωση Βάσης
                    Crud.createTableCitySearches();
                    Crud.insertDataToCitySearches(weatherData.get(0).get(0).getCity());
                } else {
                    JOptionPane.showMessageDialog(this, "Η πόλη δεν βρέθηκε ή το API είναι προσωρινά μη διαθέσιμο.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + e.getMessage());
            } 
        } else {
            JOptionPane.showMessageDialog(this, "Η επιλογή αυτή θα είναι διαθέσιμη σε μελλοντική έκδοση.");
        }
    }
    
}