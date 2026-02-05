package eap_pli24_ge3.weatherApp.main;

// In this class we will store the weather data that we need from the API, after we parse it.

public class Forecast {


    //Attributes
    private String city;
    private String date;
    private short temperature;
    private short humidity;
    private short windSpeed;
    private short uvIndex;
    private String weatherDescription;


    //Getters and Setters
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public short getTemperature() {
        return temperature;
    }

    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    public short getHumidity() {
        return humidity;
    }

    public void setHumidity(short humidity) {
        this.humidity = humidity;
    }

    public short getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(short windSpeed) {
        this.windSpeed = windSpeed;
    }

    public short getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(short uvIndex) {
        this.uvIndex = uvIndex;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }


}
