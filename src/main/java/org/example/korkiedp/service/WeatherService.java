package org.example.korkiedp.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private static final String API_KEY = "690346d65c1cc38c52f9a3d25f03455a";

    public static String getWeather(double lat, double lon) {
        String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=pl",
                lat, lon, API_KEY);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                response.append(line);

            reader.close();

            JSONObject obj = new JSONObject(response.toString());
            String weather = obj.getJSONArray("weather").getJSONObject(0).getString("description");
            double temp = obj.getJSONObject("main").getDouble("temp");

            return String.format("%s, %.1f°C", weather, temp);

        } catch (Exception e) {
            e.printStackTrace();
            return "Brak danych pogodowych";
        }
    }
}
