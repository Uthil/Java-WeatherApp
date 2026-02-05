package eap_pli24_ge3.weatherApp.db;

import eap_pli24_ge3.weatherApp.main.Forecast;
import java.sql.*;
import java.util.ArrayList;


// This class will be used to perform CRUD operations on the database.
@SuppressWarnings("ALL")
public class Crud {

    // This method will be used to create the table in the database.
    // Each row of the table will represent a specific time of day.
    // The data for each day will be stored in the columns of the table.
    public static void createTable() {
        Connection connection = ConnectDB.connect();
        System.setProperty("derby.language.sequence.preallocator", "1");
        String createSQL = "CREATE TABLE weather_forecast"
                + "(id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
                + "saveActionTime VARCHAR(30),"
                + "city VARCHAR(30),"
                + "timeOfDay VARCHAR(20),"
                + "Date0 VARCHAR(20),"
                + "temp_C0 INT,"
                + "humidity0 INT,"
                + "windspeedKmph0 INT,"
                + "uvIndex0 INT,"
                + "weatherDesc0 VARCHAR(50),"
                + "Date1 VARCHAR(20),"
                + "temp_C1 INT,"
                + "humidity1 INT,"
                + "windspeedKmph1 INT,"
                + "uvIndex1 INT,"
                + "weatherDesc1 VARCHAR(50),"
                + "Date2 VARCHAR(20),"
                + "temp_C2 INT,"
                + "humidity2 INT,"
                + "windspeedKmph2 INT,"
                + "uvIndex2 INT,"
                + "weatherDesc2 VARCHAR(50)"
                + ")";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }


    // This method will be used to create the table for the statistics.
    public static void createTableCitySearches() {
        Connection connection = ConnectDB.connect();
        String createSQL = "CREATE TABLE city_searches"
                + "(id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
                + "city VARCHAR(30),"
                + "numOfSearches INT"
                + ")";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


    // This method will be used to insert the data into the database, by using a prepared statement and a loop
    // which will insert the data row by row, one for each time of day.
    public static void insertData(ArrayList<ArrayList<Forecast>> fullForecast, String saveActionTime, String city) {
        Connection connection = ConnectDB.connect();
        // String for the SQL insert prepared statement.
        String insertSQL = "INSERT INTO weather_forecast " +
                "(saveActionTime, city, timeOfDay, " +
                "Date0, temp_C0, humidity0, windspeedKmph0, uvIndex0, weatherDesc0, " +
                "Date1, temp_C1, humidity1, windspeedKmph1, uvIndex1, weatherDesc1, " +
                "Date2, temp_C2, humidity2, windspeedKmph2, uvIndex2, weatherDesc2) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            // Set the fixed values.
            preparedStatement.setString(1, saveActionTime);
            preparedStatement.setString(2, city);

            // Loop through the fullForecast arraylist.
            // Outer loop for the time of day.
            for (int i = 0; i < 4; i++) {
                // Aux array for the time of day.
                String[] timeOfDay = new String[]{"morning", "noon", "evening", "night"};
                // Set the time of day.
                preparedStatement.setString(3, timeOfDay[i]);
                // Inner loop for the days.
                for (int j = 1; j < 4; j++) {
                    int baseIndex = (j - 1) * 6 + 4; // Calculate base index for each day
                    preparedStatement.setString(baseIndex, fullForecast.get(j).get(i).getDate());
                    preparedStatement.setShort(baseIndex + 1, fullForecast.get(j).get(i).getTemperature());
                    preparedStatement.setShort(baseIndex + 2, fullForecast.get(j).get(i).getHumidity());
                    preparedStatement.setShort(baseIndex + 3, fullForecast.get(j).get(i).getWindSpeed());
                    preparedStatement.setShort(baseIndex + 4, fullForecast.get(j).get(i).getUvIndex());
                    preparedStatement.setString(baseIndex + 5, fullForecast.get(j).get(i).getWeatherDescription());
                }
                preparedStatement.addBatch();
            }

            // Execute the batch.
            preparedStatement.executeBatch();
            preparedStatement.close();
            connection.close();

            } catch(SQLException ex){
                System.out.println(ex.getLocalizedMessage());
            }

        }


    // This method will insert new data to the statistics table.
    public static void insertDataToCitySearches(String city) {
        Connection connection = ConnectDB.connect();
        try {
            assert connection != null;
            // Check if city exists
            String checkSQL = "SELECT * FROM city_searches WHERE city = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSQL);
            checkStatement.setString(1, city);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // If city exists, increment numOfSearches
                String updateSQL = "UPDATE city_searches SET numOfSearches = numOfSearches + 1 WHERE city = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setString(1, city);
                updateStatement.executeUpdate();
                updateStatement.close();
            } else {
                // If city does not exist, insert new record
                String insertSQL = "INSERT INTO city_searches (city, numOfSearches) VALUES (?, 1)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                insertStatement.setString(1, city);
                insertStatement.executeUpdate();
                insertStatement.close();
            }
            resultSet.close();
            checkStatement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


    // This method checks if the data to be inserted already exists in the database.
    // We will check the date part of userActionTime to determine whether a save operation has been performed
    // on that same day for that specific city.
    public static boolean checkData(String saveActionTime, String city) {
        Connection connection = ConnectDB.connect();
        String selectSQL = "SELECT * FROM weather_forecast WHERE city = ? AND saveActionTime LIKE ?";

        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);

            // Set the city and the date part of the saveActionTime as parameters for the prepared statement.
            preparedStatement.setString(1, city);
            preparedStatement.setString(2, saveActionTime.split(" ")[0] + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            // If the result set is not empty, then the data already exists in the database.
            if (resultSet.next()) {
                preparedStatement.close();
                connection.close();
                return true;
            }
            preparedStatement.close();
            connection.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return false;
    }


    // This method will be used to get the latest saved forecast for a specific city (the 1st day only).
    public static ArrayList<Forecast> getLatestForecast(String city) {
        ArrayList<Forecast> latestForecast = new ArrayList<>();
        Connection connection = ConnectDB.connect();

        String selectSQL = "SELECT City, Date0, temp_C0, humidity0, windspeedKmph0, uvIndex0, weatherDesc0 FROM weather_forecast WHERE city = ? ORDER BY id DESC FETCH FIRST 4 ROWS ONLY";

        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Forecast forecast = new Forecast();

                    forecast.setCity(resultSet.getString("City"));
                    forecast.setDate(resultSet.getString("Date0"));
                    forecast.setTemperature(resultSet.getShort("temp_C0"));
                    forecast.setHumidity(resultSet.getShort("humidity0"));
                    forecast.setWindSpeed(resultSet.getShort("windspeedKmph0"));
                    forecast.setUvIndex(resultSet.getShort("uvIndex0"));
                    forecast.setWeatherDescription(resultSet.getString("weatherDesc0"));

                    latestForecast.add(0,forecast);
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();

        } catch (SQLException ex) {
            if (ex.getSQLState().equals("42X05")) {
                return null; // Table does not exist
            } else {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        return latestForecast;

    }


    // This method will be used to update the forecast for a specific city (the 1st day only) based on user input.
    public static void updateForecast(ArrayList<Forecast> editedForecast) {
        Connection connection = ConnectDB.connect();
        String updateSQL = "UPDATE weather_forecast SET temp_C0 = ?, humidity0 = ?, windspeedKmph0 = ?, uvIndex0 = ?, weatherDesc0 = ? WHERE city = ? AND Date0 = ? AND timeOfDay = ?";

        String[] timeOfDay = new String[]{"morning", "noon", "evening", "night"};

        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);

            for (Forecast forecast : editedForecast) {
                preparedStatement.setShort(1, forecast.getTemperature());
                preparedStatement.setShort(2, forecast.getHumidity());
                preparedStatement.setShort(3, forecast.getWindSpeed());
                preparedStatement.setShort(4, forecast.getUvIndex());
                preparedStatement.setString(5, forecast.getWeatherDescription());

                preparedStatement.setString(6, forecast.getCity());
                preparedStatement.setString(7, forecast.getDate());
                preparedStatement.setString(8, timeOfDay[editedForecast.indexOf(forecast)]);

                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
            connection.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


    // This method will be used to delete all data for a specific city from the database.
    public static void deleteData(String city) {
        Connection connection = ConnectDB.connect();
        String deleteSQL = "DELETE FROM weather_forecast WHERE city = ?";

        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);

            preparedStatement.setString(1, city);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


    // This method will be used to display which cities have been searched for and saved in the database.
    public static ArrayList<String> selectAllReturnCity() {
        ArrayList<String> cities = new ArrayList<>();
        Connection connection = ConnectDB.connect();
        String selectSQL = "SELECT DISTINCT city FROM weather_forecast";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                cities.add(city);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return cities;
    }


    // This method will be used to display a list of the dates when data were saved for a specific city.
    public static ArrayList<String> selectDatesForCity(String city) {
        ArrayList<String> dates = new ArrayList<>();
        Connection connection = ConnectDB.connect();
        String selectSQL = "SELECT DISTINCT Date0 FROM weather_forecast WHERE city = ? ORDER BY Date0";
        try {
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                dates.add(resultSet.getString("Date0"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return dates;
    }


    // This method will be used to print the table of the database to the console.
    public static void printTable() {
        Connection connection = ConnectDB.connect();
        String selectSQL = "SELECT * FROM weather_forecast";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Save Action Time: " + resultSet.getString("saveActionTime"));
                System.out.println("City: " + resultSet.getString("city"));
                System.out.println("Time of Day: " + resultSet.getString("timeOfDay"));
                System.out.println("Date0: " + resultSet.getString("Date0"));
                System.out.println("Temperature0: " + resultSet.getInt("temp_C0"));
                System.out.println("Humidity0: " + resultSet.getInt("humidity0"));
                System.out.println("Wind Speed0: " + resultSet.getInt("windspeedKmph0"));
                System.out.println("UV Index0: " + resultSet.getInt("uvIndex0"));
                System.out.println("Weather Description0: " + resultSet.getString("weatherDesc0"));
                System.out.println("Date1: " + resultSet.getString("Date1"));
                System.out.println("Temperature1: " + resultSet.getInt("temp_C1"));
                System.out.println("Humidity1: " + resultSet.getInt("humidity1"));
                System.out.println("Wind Speed1: " + resultSet.getInt("windspeedKmph1"));
                System.out.println("UV Index1: " + resultSet.getInt("uvIndex1"));
                System.out.println("Weather Description1: " + resultSet.getString("weatherDesc1"));
                System.out.println("Date2: " + resultSet.getString("Date2"));
                System.out.println("Temperature2: " + resultSet.getInt("temp_C2"));
                System.out.println("Humidity2: " + resultSet.getInt("humidity2"));
                System.out.println("Wind Speed2: " + resultSet.getInt("windspeedKmph2"));
                System.out.println("UV Index2: " + resultSet.getInt("uvIndex2"));
                System.out.println("Weather Description2: " + resultSet.getString("weatherDesc2"));
                System.out.println();
            }
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }


    // This method will be used to print the statistics.
    public static String selectCitySearches_toString() {
        Connection connection = ConnectDB.connect();
        String selectSQL = "SELECT city, numOfSearches FROM city_searches ORDER BY numOfSearches DESC";
        StringBuilder result = new StringBuilder();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectSQL);
            while (resultSet.next()) {
                result.append("City: ").append(resultSet.getString("city")).append(", Number of Searches: ").append(resultSet.getInt("numOfSearches")).append("\n");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return result.toString();
    }


    public static ResultSet selectCitySearches() {
    Connection connection = ConnectDB.connect();
    String selectSQL = "SELECT city, numOfSearches FROM city_searches ORDER BY numOfSearches DESC";
    ResultSet resultSet = null;
    try {
        assert connection != null;
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(selectSQL);
    } catch (SQLException ex) {
        System.out.println(ex.getLocalizedMessage());
    }
    return resultSet;
    }
    
    
    // This method will be used to drop the table from the database.
    public static void dropTable() {
        Connection connection = ConnectDB.connect();
        String dropSQL = "DROP TABLE weather_forecast";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    // This method will be used to drop the statistics table from the database.
    public static void dropTableCitySearches() {
        Connection connection = ConnectDB.connect();
        String dropSQL = "DROP TABLE city_searches";
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            statement.executeUpdate(dropSQL);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

}
