package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ForecastDisplay extends JFrame {

    // Attribute
    private final ArrayList<ArrayList<Forecast>> forecastData;
    
    private javax.swing.JTextField[][] displayFields = new javax.swing.JTextField[4][17];

    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnExport;
    private javax.swing.JLabel lblCityTitle;
    private javax.swing.JPanel mainContainer;
    private javax.swing.JPanel buttonPanel;
    
    
    // Constructor that takes the weather data as parameter.
    public ForecastDisplay(ArrayList<ArrayList<Forecast>> forecastData) {
        this.forecastData = forecastData;
        initComponents();
        setIconImage(AppLogo.setIconImage());
        displayData();
    }
    
    // This method will be used to display the forecast data in the GUI.
    @SuppressWarnings("ReassignedVariable")
    private void displayData() {
        
        if (forecastData == null || forecastData.isEmpty()) return;

        // Ενημέρωση Τίτλου Πόλης
        lblCityTitle.setText("Πρόγνωση Καιρού για την Πόλη: " + forecastData.get(0).get(0).getCity());

        // Loop για κάθε μία από τις 4 ημέρες (Σήμερα + 3 επόμενες)
        for (int day = 0; day < forecastData.size() && day < 4; day++) {
            ArrayList<Forecast> dayList = forecastData.get(day);
            if (dayList.isEmpty()) continue;
        
            Forecast f = dayList.get(0); // Παίρνουμε τα κύρια δεδομένα της ημέρας

                // Γέμισμα των 17 πεδίων του πίνακα displayFields[day][index]
                displayFields[day][0].setText(f.getTempC() + " °C");
                displayFields[day][1].setText(f.getFeelsLikeC() + " °C");
                displayFields[day][2].setText(f.getHumidity() + " %");
                displayFields[day][3].setText(f.getPressure());
                displayFields[day][4].setText(f.getWindspeedKmph() + " km/h");
                displayFields[day][5].setText(f.getWinddir16Point());
                displayFields[day][6].setText(f.getUvIndex());
                displayFields[day][7].setText(f.getVisibility() + " km");
                displayFields[day][8].setText(f.getPrecipMM() + " mm");
                displayFields[day][9].setText(f.getCloudcover() + " %");
                displayFields[day][10].setText(f.getChanceofrain() + " %");
                displayFields[day][11].setText(f.getChanceofsnow() + " %");
                displayFields[day][12].setText(f.getSunrise());
                displayFields[day][13].setText(f.getSunset());
                displayFields[day][14].setText(f.getMoonrise());
                displayFields[day][15].setText(f.getMoonset());
                displayFields[day][16].setText(f.getWeatherDesc());
            }
    }

    private void initComponents() {

        setTitle("Weather Forecast Details");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 0. Τίτλος πόλης στο πάνω μέρος
        lblCityTitle = new JLabel("", SwingConstants.CENTER);
        lblCityTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(lblCityTitle, BorderLayout.NORTH);

        // 1. Κεντρικό Panel που θα κρατάει 4 στήλες (Σήμερα + 3 ημέρες)
        JPanel mainDisplayPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        mainDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] dayTitles = {"Current Weather", "Forecast Day 1", "Forecast Day 2", "Forecast Day 3"};
        String[] rowLabels = {"Temperature (°C)", "Feels Like (°C)", "Humidity (%)", "Pressure", "Wind Speed", 
            "Wind Direction", "UV Index", "Visibility", "Precipitation", "Cloud Cover", "Chance of Rain", 
            "Chance of Snow", "Sunrise", "Sunset", "Moonrise", "Moonset", "Condition"
        };

        // 2. Loop για τη δημιουργία των 4 στηλών
        for (int day = 0; day < 4; day++) {
            JPanel columnPanel = new JPanel(new GridBagLayout());
            columnPanel.setBorder(BorderFactory.createTitledBorder(dayTitles[day]));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 5, 2, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 3. Loop για τη δημιουργία των 17 πεδίων ανά στήλη
            for (int i = 0; i < 17; i++) {
                // Προσθήκη Label (π.χ. "Temperature")
                gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
                columnPanel.add(new JLabel(rowLabels[i]), gbc);

                // Αρχικοποίηση και προσθήκη TextField στον 2D Πίνακα
                displayFields[day][i] = new JTextField(10);
                displayFields[day][i].setEditable(false);
                displayFields[day][i].setBackground(Color.WHITE);
                
                gbc.gridx = 1; gbc.weightx = 0.7;
                columnPanel.add(displayFields[day][i], gbc);
            }
            mainDisplayPanel.add(columnPanel);
        }

        // 4. Panel για τα κουμπιά (Save, Back κλπ) στο κάτω μέρος
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBack = new JButton("Επιστροφή");
        btnSave = new JButton("Αποθήκευση στη ΒΔ");
        
        btnBack.addActionListener(evt -> this.dispose());
        btnSave.addActionListener(this::btnSaveActionPerformed); // Θα το φτιάξουμε στο Βήμα 4

        buttonPanel.add(btnSave);
        buttonPanel.add(btnBack);

        // Προσθήκη όλων στο Frame
        add(mainDisplayPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);

    }

    // Inserts the data to the database if they do not already exist
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Format the LocalDateTime to a string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String userActionTime = now.format(formatter);

        // Create the table if it does not exist
        Crud.createTable();

        String city = forecastData.get(0).get(0).getCity();

        // Check if data already exists in the database
        if (Crud.checkData(userActionTime, city)) {
            JOptionPane.showMessageDialog(null,
                    "Η πρόβλεψη για αυτήν την πόλη και για τις ίδιες ημερομηνίες υπάρχει ήδη στη βάση δεδομένων.");
        } else {
            // Store the data to the database
            Crud.insertData(forecastData, userActionTime, city);
            JOptionPane.showMessageDialog(null,
                    "Η πρόβλεψη αποθηκεύτηκε επιτυχώς στη βάση δεδομένων.");
        }

        // Print table for testing
        //Crud.printTable();

    }

    
    // Opens up the EditLastSavedData window and passes the corresponding data.
    // If there are no data in the database, the user gets informed accordingly.
    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        ArrayList<Forecast> latestForecast = Crud.getLatestForecast(forecastData.get(0).get(0).getCity());

        if (latestForecast == null) {
            JOptionPane.showMessageDialog(null, "Δεν υπάρχουν αποθηκευμένα δεδομένα.");
        } else if (latestForecast.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Δεν υπάρχουν δεδομένα για αυτήν την πόλη.");
        } else {
            new EditLastSavedForecast(latestForecast).setVisible(true);
        }

    }


    // Deletes the data from the database only after the user confirms the action.
    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        String city = forecastData.get(0).get(0).getCity();
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Είστε σίγουρος/η ότι θέλετε να διαγράψετε όλα τα δεδομένα για την πόλη " + city + ";",
                "Διαγραφή",
                dialogButton);
        // If user confirms the action, delete the data
        if (dialogResult == 0) {
            Crud.deleteData(city);
            JOptionPane.showMessageDialog(null, "Τα δεδομένα διαγράφηκαν επιτυχώς από τη βάση δεδομένων.");
        }
    }


    // Deletes everything only after the user confirms the action.
    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {
        int response = JOptionPane.showConfirmDialog(null,
                "Είστε σίγουρος/η ότι θέλετε να διαγράψετε ΟΛΑ τα δεδομένα;",
                "Επιβεβαίωση",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            Crud.dropTable();
        }
    }
}
