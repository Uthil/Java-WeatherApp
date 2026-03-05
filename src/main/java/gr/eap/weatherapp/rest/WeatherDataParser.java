package gr.eap.weatherapp.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gr.eap.weatherapp.main.Forecast;
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

        // Get astronomy data for today (sunrise/sunset/moonrise/moonset) from day 0
        JsonObject todayAstronomy = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().getAsJsonArray("astronomy").get(0).getAsJsonObject();

        forecast.setCity(city);
        forecast.setDate(date);
        forecast.setTempC(currentCondition.get("temp_C").getAsInt());
        forecast.setFeelsLikeC(currentCondition.get("FeelsLikeC").getAsInt());
        forecast.setHumidity(currentCondition.get("humidity").getAsInt());
        forecast.setPressure(currentCondition.get("pressure").getAsString());
        forecast.setWindspeedKmph(currentCondition.get("windspeedKmph").getAsInt());
        forecast.setWinddir16Point(currentCondition.get("winddir16Point").getAsString());
        forecast.setUvIndex(currentCondition.get("uvIndex").getAsInt());
        forecast.setVisibility(currentCondition.get("visibility").getAsInt());
        forecast.setPrecipMM(currentCondition.get("precipMM").getAsDouble());
        forecast.setCloudcover(currentCondition.get("cloudcover").getAsInt());
        forecast.setChanceofrain(0); // Not available in current_condition
        forecast.setChanceofsnow(0); // Not available in current_condition
        forecast.setSunrise(todayAstronomy.get("sunrise").getAsString());
        forecast.setSunset(todayAstronomy.get("sunset").getAsString());
        forecast.setMoonrise(todayAstronomy.get("moonrise").getAsString());
        forecast.setMoonset(todayAstronomy.get("moonset").getAsString());
        forecast.setWeatherDesc(currentCondition.getAsJsonArray("weatherDesc").get(0).getAsJsonObject().get("value").getAsString());

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

                JsonObject hourly = dailyForecast.get(i).getAsJsonObject().getAsJsonArray("hourly").get(time_index[j]).getAsJsonObject();

                forecast.setCity(city);
                forecast.setDate(dailyForecast.get(i).getAsJsonObject().get("date").getAsString());
                forecast.setTempC(hourly.get("tempC").getAsInt());
                forecast.setFeelsLikeC(hourly.get("FeelsLikeC").getAsInt());
                forecast.setHumidity(hourly.get("humidity").getAsInt());
                forecast.setPressure(hourly.get("pressure").getAsString());
                forecast.setWindspeedKmph(hourly.get("windspeedKmph").getAsInt());
                forecast.setWinddir16Point(hourly.get("winddir16Point").getAsString());
                forecast.setUvIndex(hourly.get("uvIndex").getAsInt());
                forecast.setVisibility(hourly.get("visibility").getAsInt());
                forecast.setPrecipMM(hourly.get("precipMM").getAsDouble());
                forecast.setCloudcover(hourly.get("cloudcover").getAsInt());
                forecast.setChanceofrain(hourly.get("chanceofrain").getAsInt());
                forecast.setChanceofsnow(hourly.get("chanceofsnow").getAsInt());
                forecast.setSunrise(todayAstronomy.get("sunrise").getAsString());
                forecast.setSunset(todayAstronomy.get("sunset").getAsString());
                forecast.setMoonrise(todayAstronomy.get("moonrise").getAsString());
                forecast.setMoonset(todayAstronomy.get("moonset").getAsString());
                forecast.setWeatherDesc(hourly.getAsJsonArray("weatherDesc").get(0).getAsJsonObject().get("value").getAsString());

                // Add the forecast for each time to the daily forecast.
                fullForecast.get(i + 1).add(forecast);
            }
        }

        return fullForecast;
    }

}
