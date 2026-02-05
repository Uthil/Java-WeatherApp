package eap_pli24_ge3.weatherApp.main;


public class Main {


    public static void main(String[] args) {

        // Initialize the GUI with Nimbus look and feel.
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        new eap_pli24_ge3.weatherApp.gui.MainMenu().setVisible(true);

    }

}
