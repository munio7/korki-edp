package org.example.korkiedp.service;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyService {

    private static final String BASE_URL = "https://api.nbp.pl/api/exchangerates/rates/A/%s/?format=json";

    // Zwraca kurs waluty na podstawie jej kodu (np. "USD", "EUR")
    public static double getExchangeRate(String currencyCode) {
        try {
            String urlString = String.format(BASE_URL, currencyCode.toUpperCase());
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            JSONObject response = new JSONObject(json.toString());
            return response.getJSONArray("rates").getJSONObject(0).getDouble("mid");

        } catch (Exception e) {
            System.err.println("Błąd pobierania kursu waluty: " + e.getMessage());
            return -1;
        }
    }

    // Przelicza daną kwotę z waluty obcej na PLN
    public static double convertToPLNFrom(String currencyCode, double amount) {
        double rate = getExchangeRate(currencyCode);
        if (rate == -1) {
            throw new RuntimeException("Nie udało się pobrać kursu waluty: " + currencyCode);
        }
        return amount * rate;
    }
}
