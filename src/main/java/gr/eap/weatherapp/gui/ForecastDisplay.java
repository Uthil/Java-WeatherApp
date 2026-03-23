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

    private JLabel lblCity; //City Title Label
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
        lblCity.setText(forecastData.get(0).get(0).getCity().toUpperCase());
        
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

        setTitle("Weather Forecast - 3 Day Display");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(CLR_BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
 
        pack();
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    // επικεφαλίδα με εικονίδιο και τίτλο
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, CLR_HEADER_TOP, 0, getHeight(), CLR_HEADER_BOT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(18, 30, 18, 30));

        // Εικονίδιο και Τίτλος
        JLabel lblIcon = new JLabel("🌤");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
 
        lblCity = new JLabel("", SwingConstants.CENTER);
        lblCity.setFont(FONT_TITLE);
        lblCity.setForeground(CLR_WHITE);
 
        JLabel lblSub = new JLabel("3-DAY WEATHER FORECAST", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(CLR_TEXT_LIGHT);
 
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setOpaque(false);
        textPanel.add(lblCity);
        textPanel.add(lblSub);
 
        header.add(lblIcon, BorderLayout.WEST);
        header.add(textPanel, BorderLayout.CENTER);
 
        return header;
    }

    // Κύριο πάνελ με τα δεδομένα της πρόβλεψης
    private JPanel buildMainPanel() {
        JPanel main = new JPanel(new GridLayout(3, 1, 0, 8));
        main.setBackground(CLR_BG);
        main.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
 
        String[] dayLabels = {"Day 1", "Day 2", "Day 3"};
 
        for (int day = 0; day < 3; day++) {
            main.add(buildDayPanel(day, dayLabels[day]));
        }
 
        return main;
    }

    // Πάνελ μιας ημέρας με ημερομηνία και 4 χρονικές στιγμές
    private JPanel buildDayPanel(int dayIndex, String dayLabel) {
        JPanel outer = new JPanel(new BorderLayout(0, 4));
        outer.setBackground(CLR_WHITE);
        outer.setBorder(new CompoundBorder(
            new LineBorder(CLR_FIELD_BORDER, 1, true),
            BorderFactory.createEmptyBorder(0, 0, 6, 0)
        ));

        // Επικεφαλίδα ημέρας
        JPanel dayHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(CLR_DAY_HEADER);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dayHeader.setOpaque(false);

        JLabel lblDayName = new JLabel(dayLabel);
        lblDayName.setFont(FONT_DAY);
        lblDayName.setForeground(CLR_WHITE);
 
        lblDates[dayIndex] = new JLabel("—");
        lblDates[dayIndex].setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDates[dayIndex].setForeground(CLR_TEXT_LIGHT);
 
        dayHeader.add(lblDayName);
        dayHeader.add(new JLabel("  |  ") {{ setForeground(CLR_TEXT_LIGHT); }});
        dayHeader.add(lblDates[dayIndex]);
 
        // Πάνελ με τις 4 χρονικές στιγμές (4 στήλες)
        JPanel colsPanel = new JPanel(new GridBagLayout());
        colsPanel.setBackground(CLR_WHITE);
        colsPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 4, 2, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        String[] timeLabels  = {"Morning", "Noon", "Evening", "Night"};
        String[] fieldLabels = {"Temperature (°C)", "Humidity (%)", "Wind (km/h)", "UV Index", "Condition"};
        
        // Επικεφαλίδες στηλών
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0.18;
        colsPanel.add(new JLabel(""), gbc);
 
        for (int t = 0; t < 4; t++) {
            JLabel tlbl = new JLabel(timeLabels[t], SwingConstants.CENTER);
            tlbl.setFont(FONT_COL);
            tlbl.setForeground(CLR_DAY_HEADER);
            tlbl.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, CLR_COL_HEADER));
            gbc.gridx = t + 1; gbc.weightx = 0.205;
            colsPanel.add(tlbl, gbc);
        }

        // Πεδία δεδομένων
        for (int f = 0; f < 5; f++) {
            gbc.gridy = f + 1;
 
            // Row label
            JLabel lbl = new JLabel(fieldLabels[f]);
            lbl.setFont(FONT_LABEL);
            lbl.setForeground(new Color(80, 100, 130));
            gbc.gridx = 0; gbc.weightx = 0.18;
            colsPanel.add(lbl, gbc);

            // Πεδία για κάθε χρονική στιγμή
             for (int t = 0; t < 4; t++) {
                fields[dayIndex][t][f] = new JTextField(8);
                fields[dayIndex][t][f].setEditable(false);
                fields[dayIndex][t][f].setFont(FONT_FIELD);
                fields[dayIndex][t][f].setBackground(CLR_FIELD_BG);
                fields[dayIndex][t][f].setForeground(new Color(30, 60, 100));
                fields[dayIndex][t][f].setBorder(new CompoundBorder(
                    new LineBorder(CLR_FIELD_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
                ));
                fields[dayIndex][t][f].setHorizontalAlignment(JTextField.CENTER);
                gbc.gridx = t + 1; gbc.weightx = 0.205;
                colsPanel.add(fields[dayIndex][t][f], gbc);
            }
        }
        outer.add(dayHeader, BorderLayout.NORTH);
        outer.add(colsPanel, BorderLayout.CENTER);
 
        return outer;
    }

    // Πάνελ με τα κουμπιά αποθήκευσης, επεξεργασίας και διαγραφής
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(CLR_BG);
        panel.setBorder(new MatteBorder(1, 0, 0, 0, CLR_FIELD_BORDER));
 
        btnSave = styledButton("Αποθήκευση", CLR_BTN_PRIMARY);
        btnEdit = styledButton("Επεξεργασία", CLR_BTN_PRIMARY);
        btnDelete = styledButton("Διαγραφή", CLR_BTN_DANGER);
        btnDeleteAll = styledButton("⚠  Διαγραφή Όλων", CLR_BTN_DANGER);
 
        btnSave.addActionListener(evt -> btnSaveActionPerformed(evt));
        btnEdit.addActionListener(evt -> btnEditActionPerformed(evt));
        btnDelete.addActionListener(evt -> btnDeleteActionPerformed(evt));
        btnDeleteAll.addActionListener(evt -> btnDeleteAllActionPerformed(evt));
 
        panel.add(btnSave);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnDeleteAll);
 
        return panel;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() :
                            getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(CLR_WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
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
