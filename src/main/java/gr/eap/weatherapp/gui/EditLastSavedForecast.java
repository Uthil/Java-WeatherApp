package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.AppLogo;
import gr.eap.weatherapp.main.Forecast;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class EditLastSavedForecast extends JFrame {

    // Attributes
    private final ArrayList<Forecast> latestForecast;
    private JTextField[][] editFields = new JTextField[4][5]; // Aux array to place the textfields in order.

    private JTextField txtCity;
    private JTextField txtDate;
    private JButton btnSave;

    // Constructor that takes the last saved forecast as an argument.
    public EditLastSavedForecast(ArrayList<Forecast> latestForecast) {
        this.latestForecast = latestForecast;
        initComponents();
        setIconImage(AppLogo.setIconImage());
        displayForecastData();
    }

    // This method will be used to display the forecast data in the GUI.
    private void displayForecastData() {
        txtCity.setText(latestForecast.get(0).getCity());
        txtDate.setText(latestForecast.get(0).getDate());

        // Display the forecast data in the textfields.
        for (int i=0; i<latestForecast.size(); i++) {
            Forecast f = latestForecast.get(i);
            editFields[i][0].setText(String.valueOf(f.getTempC()));
            editFields[i][1].setText(String.valueOf(f.getHumidity()));
            editFields[i][2].setText(String.valueOf(f.getWindspeedKmph()));
            editFields[i][3].setText(String.valueOf(f.getUvIndex()));
            editFields[i][4].setText(String.valueOf(f.getWeatherDesc()));
        }
    }

    // This method will be used to get the updated forecast data from the textfields after the user has edited them.
    private ArrayList<Forecast> getUpdatedForecast() {
        ArrayList<Forecast> updatedForecast = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Forecast forecast = new Forecast();
            forecast.setCity(latestForecast.get(i).getCity());
            forecast.setDate(latestForecast.get(i).getDate());

            // If the user has left a field blank, the current value will remain unchanged.
            forecast.setTempC(editFields[i][0].getText().isEmpty() ? latestForecast.get(i).getTempC() : Integer.parseInt(editFields[i][0].getText()));
            forecast.setHumidity(editFields[i][1].getText().isEmpty() ? latestForecast.get(i).getHumidity() : Integer.parseInt(editFields[i][1].getText()));
            forecast.setWindspeedKmph(editFields[i][2].getText().isEmpty() ? latestForecast.get(i).getWindspeedKmph() : Integer.parseInt(editFields[i][2].getText()));
            forecast.setUvIndex(editFields[i][3].getText().isEmpty() ? latestForecast.get(i).getUvIndex() : Integer.parseInt(editFields[i][3].getText()));
            forecast.setWeatherDesc(editFields[i][4].getText().isEmpty() ? latestForecast.get(i).getWeatherDesc() : editFields[i][4].getText());

            updatedForecast.add(forecast);
        }

        return updatedForecast;

    }

    private void initComponents() {

        setTitle("GMF Weather Application");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
 
        // 0. Header panel - City & Date
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JTextField lblStatic = new JTextField("ΤΕΛΕΥΤΑΙΑ ΚΑΤΑΧΩΡΙΣΗ ΓΙΑ:");
        lblStatic.setEditable(false);
        lblStatic.setHorizontalAlignment(JTextField.CENTER);
 
        txtCity = new JTextField(15);
        txtCity.setEditable(false);
        txtCity.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 13));
        txtCity.setHorizontalAlignment(JTextField.CENTER);
 
        txtDate = new JTextField(10);
        txtDate.setEditable(false);
        txtDate.setHorizontalAlignment(JTextField.CENTER);
 
        headerPanel.add(lblStatic);
        headerPanel.add(txtCity);
        headerPanel.add(txtDate);
        add(headerPanel, BorderLayout.NORTH);
 
        // 1. Main panel - 4 columns (Morning/Noon/Evening/Night)
        JPanel mainPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        String[] timeTitles = {"Πρωί", "Μεσημέρι", "Απόγευμα", "Νύχτα"};
        String[] rowLabels = {"Θερμοκρασία (°C)", "Υγρασία (%)", "Άνεμος (km/h)", "UV Index", "Καιρός"};
 
        for (int col = 0; col < 4; col++) {
            JPanel colPanel = new JPanel(new GridBagLayout());
            colPanel.setBorder(BorderFactory.createTitledBorder(timeTitles[col]));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 5, 3, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
 
            for (int row = 0; row < 5; row++) {
                gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.4;
                colPanel.add(new JLabel(rowLabels[row]), gbc);
 
                editFields[col][row] = new JTextField(8);
                gbc.gridx = 1; gbc.weightx = 0.6;
                colPanel.add(editFields[col][row], gbc);
            }
            mainPanel.add(colPanel);
        }
        add(mainPanel, BorderLayout.CENTER);

        // 2. Lower panel - instructions + button
        JPanel southPanel = new JPanel(new BorderLayout(10, 0));
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
 
        JTextPane instructions = new JTextPane();
        instructions.setEditable(false);
        instructions.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        instructions.setText("Εδώ μπορείτε να επεξεργαστείτε τις τιμές των καιρικών δεδομένων για την ημέρα της τελευταίας αποθήκευσης που πραγματοποιήσατε για αυτήν την πόλη.");
        JScrollPane scrollPane = new JScrollPane(instructions);
        scrollPane.setPreferredSize(new Dimension(300, 60));
 
        btnSave = new JButton("Αποθήκευση");
        btnSave.addActionListener(evt -> btnSaveActionPerformed(evt));
 
        southPanel.add(scrollPane, BorderLayout.CENTER);
        southPanel.add(btnSave, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
 
        pack();
        setLocationRelativeTo(null);
    }

    // Inserts the updated forecast data into the database only after the user has confirmed the changes.
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
            ArrayList<Forecast> updatedForecast = getUpdatedForecast();

            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this,
                    "Είστε σίγουρος/η ότι θέλετε να ενημερώσετε τα δεδομένα;",
                    "Επιβεβαίωση",
                    dialogButton);

            if (dialogResult == JOptionPane.YES_OPTION) {
                Crud.updateForecast(updatedForecast);
                JOptionPane.showMessageDialog(null, "Τα δεδομένα ενημερώθηκαν επιτυχώς.");
            }
    }

}
