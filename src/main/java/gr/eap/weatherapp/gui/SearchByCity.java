package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.AppLogo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class SearchByCity extends JFrame {

    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lblCity;
    private javax.swing.JTextField txtCity;

    public SearchByCity() {
        initComponents();
        setIconImage(AppLogo.setIconImage());
    }

    private void initComponents() {

        lblCity = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setTitle("GMF Weather Application");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        //LABEL
        lblCity = new JLabel("ΠΟΛΗ ΠΡΟΣ ΑΝΑΖΗΤΗΣΗ", SwingConstants.CENTER);
        lblCity.setFont(new Font("Segoe UI", Font.BOLD, 12)); // NOI18N
        
        //TEXTFIELD
        txtCity = new JTextField(20);

        //BUTTON
        btnSearch = new JButton("Αναζήτηση");
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        
        //PANEL
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        centerPanel.add(lblCity, gbc);
        gbc.gridy = 1;
        centerPanel.add(txtCity, gbc);
        gbc.gridy = 2;
        centerPanel.add(btnSearch, gbc);

        add(centerPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String city = txtCity.getText();
        ArrayList<String> dates = Crud.selectDatesForCity(city);
        if (dates.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Δεν βρέθηκαν εγγραφές για αυτήν την πόλη.");
        } else {
            StringBuilder datesText = new StringBuilder();
            for (String date : dates)
                datesText.append(date).append("\n");
            new DatesList(datesText).setVisible(true);
        }
    }

}
