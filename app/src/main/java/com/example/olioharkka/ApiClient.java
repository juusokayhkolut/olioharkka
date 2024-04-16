package com.example.olioharkka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiClient {
        private static String getAreaCode(String municipality) {
            String filePath = "kunta_koodi.csv";
            String areaCode = "";

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String code = parts[2].toLowerCase();

                    if (code.equals(municipality.toLowerCase())) {
                        areaCode = "KU" + parts[0];
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return areaCode;
        }

        public static CompletableFuture<Integer> searchForMunicipalityPopulation(String municipality) {


            return CompletableFuture.supplyAsync(() -> {
                int population = 0;
                try {
                    URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/vaerak/statfin_vaerak_pxt_11ra.px");
                    URL url = uri.toURL();
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setDoOutput(true);

                    String jsonInputString = "{"
                            + "\"query\": ["
                            + "    {\"code\": \"Alue\", \"selection\": {\"filter\": \"item\", \"values\": [" + getAreaCode(municipality) + "]}},"
                            + "    {\"code\": \"Tiedot\", \"selection\": {\"filter\": \"item\", \"values\": [\"vaesto\"]}},"
                            + "    {\"code\": \"Vuosi\", \"selection\": {\"filter\": \"item\", \"values\": [\"2022\"]}}"
                            + "  ],"
                            + "  \"response\": {\"format\": \"json-stat2\"}"
                            + "}";

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    StringBuilder responseContent = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseContent.append(line);
                        }
                    }

                    JSONArray populationList = new JSONObject(responseContent.toString()).getJSONArray("value");

                    population = Integer.parseInt(populationList.toString().replace("[", "").replace("]", "")); // [VALUE] => VALUE
                } catch (Exception e) {
                    e.printStackTrace();
                    //jsonResponse.put("error", "Error during request: " + e.getMessage());
                    System.out.println(e);
                }
                return population;
            });
        }
}
