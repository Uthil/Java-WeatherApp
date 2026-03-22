package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ForecastDisplay extends JFrame {

    //Setting color scheme for the GUI
    private static final Color CLR_BG = new Color(235, 242, 250);
    private static final Color CLR_HEADER_TOP = new Color(30,  90,  160);
    private static final Color CLR_HEADER_BOT = new Color(60, 140, 210);
    private static final Color CLR_DAY_HEADER = new Color(50, 110, 180);
    private static final Color CLR_COL_HEADER = new Color(100, 160, 220);
    private static final Color CLR_FIELD_BG = new Color(245, 250, 255);
    private static final Color CLR_FIELD_BORDER = new Color(180, 210, 240);
    private static final Color CLR_BTN_PRIMARY = new Color(30,  90,  160);
    private static final Color CLR_BTN_DANGER = new Color(190,  50,  50);
    private static final Color CLR_WHITE = Color.WHITE;
    private static final Color CLR_TEXT_LIGHT = new Color(240, 248, 255);
    
    //Setting font scheme for the GUI
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_DAY = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_COL = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 12);

    // Attribute
    private final ArrayList<ArrayList<Forecast>> forecastData;
    private final JTextField[][][] fields = new JTextField[3][4][5];

    private final JLabel[] lblDates = new JLabel[3];

    private JLabel lblCityTitle; //City Title Label
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAll;
    
    
    // Constructor that takes the weather data as parameter.
    public ForecastDisplay(ArrayList<ArrayList<Forecast>> forecastData) {
        this.forecastData = forecastData;
        initComponents();
        setIconImage(AppLogo.setIconImage());
        displayData();
    }
    
    // This method will be used to display the forecast data in the GUI.
    private void displayData() {
        
        if (forecastData == null || forecastData.isEmpty()) return;

        // Ενημέρωση Τίτλου Πόλης
        lblCityTitle.setText(forecastData.get(0).get(0).getCity().toUpperCase());
        String[] fieldNames = {"Temperature (°C)", "Humidity (%)", "Wind Speed (km/h)", "UV Index", "Condition"};
        
        // Loop για κάθε μία από τις 4 ημέρες (Σήμερα + 3 επόμενες)
        for (int day = 0; day < 3; day++) {
            ArrayList<Forecast> dayList = forecastData.get(day + 1);
            if (dayList == null || dayList.isEmpty()) continue;
 
            lblDates[day].setText(dayList.get(0).getDate());
        
                 // Γέμισμα των 17 πεδίων του πίνακα displayFields[day][index]
            for (int time = 0; time < 4 && time < dayList.size(); time++) {
                Forecast f = dayList.get(time);
                fields[day][time][0].setText(String.valueOf(f.getTempC()));
                fields[day][time][1].setText(String.valueOf(f.getHumidity()));
                fields[day][time][2].setText(String.valueOf(f.getWindspeedKmph()));
                fields[day][time][3].setText(String.valueOf(f.getUvIndex()));
                fields[day][time][4].setText(f.getWeatherDesc());
            }
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
        btnSave = new JButton("Αποθήκευση");
        btnEdit = new JButton("Επεξεργασία");
        btnDelete = new JButton("Διαγραφή");
        btnDeleteAll = new JButton("Διαγραφή Όλων");
        
        btnSave.addActionListener(this::btnSaveActionPerformed);
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        btnDeleteAll.addActionListener(this::btnDeleteAllActionPerformed);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnDeleteAll);

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
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
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
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
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
    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {
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
