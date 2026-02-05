package eap_pli24_ge3.weatherApp.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eap_pli24_ge3.weatherApp.main.Forecast;
import java.util.ArrayList;


// In this class we will parse the weather data that we want from the API.

public class WeatherDataParser {

/*
 * We will use a 2D arraylist to store the full forecast.
 * The first dimension will represent the days:
 *      current condition / today / tomorrow / the day after
 * The second dimension will represent the time of day:
 *      - time of call for the current condition,
 *      - 09:00 (Morning) / 12:00 (Noon) / 18:00 (Evening) / 21:00 (Night) for the daily forecast.
 */
    @SuppressWarnings("ReassignedVariable")
    public ArrayList<ArrayList<Forecast>> parseWeatherData(String jsonResponse) {

        ArrayList<ArrayList<Forecast>> fullForecast = new ArrayList<>(4);
        Forecast forecast = new Forecast();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonResponse).getAsJsonObject();

        // Get the city from the JSON object.
        String city = jsonObject.getAsJsonArray("nearest_area").get(0).getAsJsonObject().getAsJsonArray("areaName").get(0).getAsJsonObject().get("value").getAsString();

        // Get the current condition from the JSON object.
        JsonObject currentCondition = jsonObject.getAsJsonArray("current_condition").get(0).getAsJsonObject();

        String dateTime = currentCondition.get("localObsDateTime").getAsString();
        String date = dateTime.split(" ")[0];
        forecast.setCity(city);
        forecast.setDate(date);
        forecast.setTemperature(currentCondition.get("temp_C").getAsShort());
        forecast.setHumidity(currentCondition.get("humidity").getAsShort());
        forecast.setWindSpeed(currentCondition.get("windspeedKmph").getAsShort());
        forecast.setUvIndex(currentCondition.get("uvIndex").getAsShort());
        forecast.setWeatherDescription(currentCondition.getAsJsonArray("weatherDesc").get(0).getAsJsonObject().get("value").getAsString());

        // Add the current condition to the full forecast.
        fullForecast.add(new ArrayList<>(1));
        fullForecast.get(0).add(forecast);


        // Get the forecast for the next 3 days from the JSON object.
        JsonArray dailyForecast = jsonObject.getAsJsonArray("weather");

        for (int i = 0; i < 3; i++) {
            // Add a new arraylist for each day.
            fullForecast.add(new ArrayList<>(4));
            // Get the forecast for each day at 09:00, 12:00, 18:00 and 21:00.
            int[] time_index = new int[]{3, 4, 6, 7};
            for (int j = 0; j < 4; j++) {
                forecast = new Forecast(); // New instance of forecast for each time.

                forecast.setCity(city);
                forecast.setDate(dailyForecast.get(i).getAsJsonObject().get("date").getAsString());
                forecast.setTemperature(dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject().get("tempC").getAsShort());
                forecast.setHumidity(dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject().get("humidity").getAsShort());
                forecast.setWindSpeed(dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject().get("windspeedKmph").getAsShort());
                forecast.setUvIndex(dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject().get("uvIndex").getAsShort());
                forecast.setWeatherDescription(dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject().getAsJsonArray("weatherDesc").get(0).getAsJsonObject().get("value").getAsString());

                // Add the forecast for each time to the daily forecast.
                fullForecast.get(i + 1).add(forecast);
            }
        }

        return fullForecast;
    }

}
