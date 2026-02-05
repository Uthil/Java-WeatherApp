package eap_pli24_ge3.weatherApp.rest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

// In this class, we will make the HTTP call to the weather API using OkHttp.
public class HttpCall {

    // This method takes the URL that we want to call according to the user's input and returns the response.
    // The response will come in the form of a JSON object.
    public static String callAPI(String UrlToCall) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(UrlToCall).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
        return null;
    }

}
