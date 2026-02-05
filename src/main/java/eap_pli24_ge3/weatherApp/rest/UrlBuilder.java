package eap_pli24_ge3.weatherApp.rest;

/*
 This class will be used to build the URL for the API call.
 The URL will be built according to the user's input.
*/

public class UrlBuilder {

    private static final String BASE_Url = "https://wttr.in/";
    private static final String FORMAT = "?format=j1";

    // This method takes the city name as a parameter and returns the URL.
    public static String buildUrl(String city) {
        return BASE_Url + city + FORMAT;
    }

}
