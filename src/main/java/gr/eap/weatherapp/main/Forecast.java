package gr.eap.weatherapp.main;

// In this class we will store the weather data that we need from the API, after we parse it.

public class Forecast {


    //Attributes
    private String city;
    private String date;
   
   //Temperature
    private int tempC;
    private int feelsLikeC;
   
   //Atmospheric
    private int humidity;
    private String pressure;
    private int windspeedKmph;
    private String winddir16Point;
    private int uvIndex;
    private int visibility;
    private double precipMM;
    private int cloudcover;

    // Chance of events
    private int chanceofrain;
    private int chanceofsnow;

    // Astronomy
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;

    private String weatherDesc;


    //Getters and Setters
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getTempC() { return tempC; }
    public void setTempC(int tempC) { this.tempC = tempC; }

    public int getFeelsLikeC() { return feelsLikeC; }
    public void setFeelsLikeC(int feelsLikeC) { this.feelsLikeC = feelsLikeC; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public String getPressure() { return pressure; }
    public void setPressure(String pressure) { this.pressure = pressure; }

    public int getWindspeedKmph() { return windspeedKmph; }
    public void setWindspeedKmph(int windspeedKmph) { this.windspeedKmph = windspeedKmph; }

    public String getWinddir16Point() { return winddir16Point; }
    public void setWinddir16Point(String winddir16Point) { this.winddir16Point = winddir16Point; }

    public int getUvIndex() { return uvIndex; }
    public void setUvIndex(int uvIndex) { this.uvIndex = uvIndex; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public double getPrecipMM() { return precipMM; }
    public void setPrecipMM(double precipMM) { this.precipMM = precipMM; }

    public int getCloudcover() { return cloudcover; }
    public void setCloudcover(int cloudcover) { this.cloudcover = cloudcover; }

    public int getChanceofrain() { return chanceofrain; }
    public void setChanceofrain(int chanceofrain) { this.chanceofrain = chanceofrain; }

    public int getChanceofsnow() { return chanceofsnow; }
    public void setChanceofsnow(int chanceofsnow) { this.chanceofsnow = chanceofsnow; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }

    public String getMoonrise() { return moonrise; }
    public void setMoonrise(String moonrise) { this.moonrise = moonrise; }

    public String getMoonset() { return moonset; }
    public void setMoonset(String moonset) { this.moonset = moonset; }

    public String getWeatherDesc() { return weatherDesc; }
    public void setWeatherDesc(String weatherDesc) { this.weatherDesc = weatherDesc; }
}
