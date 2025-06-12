package org.example.korkiedp.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

        private static final Dotenv dotenv = Dotenv.load();

        public static final String API_KEY = dotenv.get("API_KEY");

    public static String getWeather(double lat, double lon) {
        String urlString = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%.6f&lon=%.6f&appid=%s&units=metric&lang=pl",
                lat, lon, WeatherService.API_KEY);

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
