package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.AppLogo;
import gr.eap.weatherapp.main.Forecast;
import gr.eap.weatherapp.rest.*;
import java.util.ArrayList;


public class SearchOptions extends javax.swing.JFrame {

    // Δηλώσεις μεταβλητών
    private javax.swing.ButtonGroup searchTypeGroup;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblInputPrompt;
    private javax.swing.JRadioButton rbCity;
    private javax.swing.JRadioButton rbLocation;
    private javax.swing.JRadioButton rbAreaCode;
    private javax.swing.JRadioButton rbCoordinates;
    private javax.swing.JRadioButton rbAirportCode;
    private javax.swing.JTextField txtCityInput;

    public SearchOptions() {
        initComponents();
        setIconImage(AppLogo.setIconImage());
    }

    private void initComponents() {

        setTitle("Search Weather Data");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        
        // Τα κενά (padding) μεταξύ των στοιχείων
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);

        // 1. Τίτλος (lblTitle)
        lblTitle = new javax.swing.JLabel("Εισάγετε το όνομα της πόλης:");
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Καταλαμβάνει 2 στήλες
        add(lblTitle, gbc);

        // 2. Πεδίο Εισαγωγής (txtCityInput)
        txtCityInput = new javax.swing.JTextField(20);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(txtCityInput, gbc);

        // 3. Κουμπί Αναζήτησης (btnSearch)
        btnSearch = new javax.swing.JButton("Αναζήτηση");
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; // Επιστροφή σε 1 στήλη
        add(btnSearch, gbc);

        // 4. Κουμπί Επιστροφής (btnBack)
        btnBack = new javax.swing.JButton("Επιστροφή");
        btnBack.addActionListener(this::btnBackActionPerformed);
        gbc.gridx = 1; gbc.gridy = 2;
        add(btnBack, gbc);

        pack();
        setLocationRelativeTo(null);
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    // Initiates the data retrieval process and passes the data to the ForecastDisplay form.
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {

        if (rbCity.isSelected()) {
            String city = txtCityInput.getText();
            WeatherDataParser parser = new WeatherDataParser();
            ArrayList<ArrayList<Forecast>> weatherData = parser.parseWeatherData(HttpCall.callAPI(UrlBuilder.buildUrl(city)));
            new ForecastDisplay(weatherData).setVisible(true);

            // Create the statistics table if it does not exist
            Crud.createTableCitySearches();
            Crud.insertDataToCitySearches(weatherData.get(0).get(0).getCity());

            System.out.println("Initiated the data retrieval process and passed the data to the ForecastDisplay form."); // Checkpoint
            }
        }  
}