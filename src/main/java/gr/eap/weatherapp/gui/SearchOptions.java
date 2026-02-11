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

        searchTypeGroup = new javax.swing.ButtonGroup();
        rbCity = new javax.swing.JRadioButton();
        lblHeader = new javax.swing.JLabel();
        rbLocation = new javax.swing.JRadioButton();
        rbAreaCode = new javax.swing.JRadioButton();
        rbCoordinates = new javax.swing.JRadioButton();
        rbAirportCode = new javax.swing.JRadioButton();
        btnSearch = new javax.swing.JButton();
        txtCityInput = new javax.swing.JTextField();
        lblInputPrompt = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("GMF Weather Application");

        searchTypeGroup.add(rbCity);
        rbCity.setText("Πόλη");
        rbCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("ΕΠΙΛΕΞΕ ΤΡΟΠΟ ΑΝΑΖΗΤΗΣΗΣ ΚΑΙΡΟΥ");

        searchTypeGroup.add(rbLocation);
        rbLocation.setText("Τοποθεσία");

        searchTypeGroup.add(rbAreaCode);
        rbAreaCode.setText("Κωδικός Περιοχής");
        rbAreaCode.setToolTipText("");

        searchTypeGroup.add(rbCoordinates);
        rbCoordinates.setText("Γεωγραφικές Συντεταγμένες");

        searchTypeGroup.add(rbAirportCode);
        rbAirportCode.setText("Κωδικός Αεροδρομίου");

        btnSearch.setText("Αναζήτηση");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lblInputPrompt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblInputPrompt.setText("ΣΤΟΙΧΕΙΟ ΠΡΟΣ ΑΝΑΖΗΤΗΣΗ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtCityInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInputPrompt))
                        .addGap(41, 41, 41)
                        .addComponent(btnSearch))
                    .addComponent(rbLocation)
                    .addComponent(rbAirportCode)
                    .addComponent(rbCoordinates, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbAreaCode)
                    .addComponent(rbCity))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblHeader)
                .addGap(18, 18, 18)
                .addComponent(rbCity)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbLocation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbAreaCode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbCoordinates)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rbAirportCode)
                .addGap(18, 18, 18)
                .addComponent(lblInputPrompt)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSearch)
                    .addComponent(txtCityInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

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