package eap_pli24_ge3.weatherApp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    // Connect to the database
    public static Connection connect() {
        String connectionString = "jdbc:derby:weatherAppDB;create=true";

        try {
            return DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return null;
    }

}