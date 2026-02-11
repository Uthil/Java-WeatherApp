package gr.eap.weatherapp.gui;

import gr.eap.weatherapp.db.Crud;
import gr.eap.weatherapp.main.AppLogo;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainMenu extends javax.swing.JFrame {

    // Δηλώσεις μεταβλητών
    private javax.swing.JButton btnWeatherData;
    private javax.swing.JButton btnCityList;
    private javax.swing.JButton btnDateList;
    private javax.swing.JButton btnStats;
    private javax.swing.JButton btnExit;

    public MainMenu() {
        initComponents();
        setIconImage(AppLogo.setIconImage());
    }

    private void initComponents() {
        // 1. Ρύθμιση παραθύρου (τίτλος, κλείσιμο, μέγεθος)
        setTitle("GMF Weather Application - Main Menu");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
    
        // 1.1 Επαναφορά background εικόνας
        java.net.URL imgUrl = getClass().getResource("/360_F1.jpg");

        if (imgUrl!= null) {
            javax.swing.ImageIcon bgIcon = new javax.swing.ImageIcon(imgUrl);
            javax.swing.JLabel background = new javax.swing.JLabel(bgIcon);
            background.setLayout(new java.awt.BorderLayout());
            this.setContentPane(background);
        } else {
            System.err.println("Resource not found: /360_F1.jpg");
        }
        
        // 2. Δημιουργία κύριου Panel με GridLayout (5 γραμμές, 1 στήλη, 15px κενό)
        javax.swing.JPanel menuPanel = new javax.swing.JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new java.awt.GridLayout(5, 1, 15, 15));
        menuPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 90, 30, 90));

        // 3. Αρχικοποίηση κουμπιών με τις αντίστοιχες ετικέτες
        btnWeatherData = new javax.swing.JButton("Προβολή καιρικών δεδομένων πόλης");
        btnCityList = new javax.swing.JButton("Λίστα πόλεων αναζήτησης");
        btnDateList = new javax.swing.JButton("Λίστα ημερομηνιών αναζήτησης");
        btnStats = new javax.swing.JButton("Στατιστικά & Εκτύπωση PDF");
        btnExit = new javax.swing.JButton("Έξοδος");

        // 4. Σύνδεση με τις ήδη υπάρχουσες Action methods των κουμπιών
        btnWeatherData.addActionListener(this::btnWeatherDataActionPerformed);
        btnCityList.addActionListener(this::btnCityListActionPerformed);
        btnDateList.addActionListener(this::btnDateListActionPerformed);
        btnStats.addActionListener(this::btnStatsActionPerformed);
        btnExit.addActionListener(this::btnExitActionPerformed);

        // 5. Προσθήκη στο Layout 
        menuPanel.add(btnWeatherData);
        menuPanel.add(menuPanel.add(btnCityList));
        menuPanel.add(btnDateList);
        menuPanel.add(btnStats);
        menuPanel.add(btnExit);

        add(menuPanel, java.awt.BorderLayout.CENTER);
        pack(); // Προσαρμογή μεγέθους βάσει περιεχομένου
        setLocationRelativeTo(null); // Κεντράρισμα
        
    }
    
    //Main menu is disabled as long the new frames are opened, and re-activated back when closed
    private void btnWeatherDataActionPerformed(java.awt.event.ActionEvent evt){
        initialize(new SearchOptions());
    }

    
    private void btnCityListActionPerformed(java.awt.event.ActionEvent evt) {
        initialize(new PreviousSavesList());
    }

    
    private void btnDateListActionPerformed(java.awt.event.ActionEvent evt) {
        initialize(new SearchByCity());
    }

    
    private void btnStatsActionPerformed(java.awt.event.ActionEvent evt) {
        ResultSet rs = Crud.selectCitySearches();
        try {
            if (rs != null && rs.next()) {
                initialize(new CitySearchesList());
            } else {
                JOptionPane.showMessageDialog(null, "Δεν βρέθηκαν δεδομένα.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CitySearchesList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    // Method to initialize the new frame and disable the main menu until the new frame is closed.
    private void initialize(JFrame frame) {
        frame.setVisible(true);
        this.setEnabled(false);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                MainMenu.this.setEnabled(true);
            }
        });
    }


    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }
}