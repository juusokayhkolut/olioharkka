package com.example.olioharkka;

import android.content.Context;
import android.content.res.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiClient {

    // for parsing json
    public class ResponseData {
        @SerializedName("dimension")
        private Map<String, Object> dimension;

        public Map<String, Object> getDimension() {
            return dimension;
        }
    }

    public static <K, V> Optional<K> getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }


        private static String getAreaCode(String municipality) {
            String areaCode = "";

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Resources.getSystem().openRawResource(R.raw.kunta_koodi)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String code = parts[2].toLowerCase();

                    System.out.println(code);

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

    public static CompletableFuture<Map<String, Object>> getPoliticalSpread(String municipality) {
        // returns ["KOK": 100, "SDP": 100] etc

        return CompletableFuture.supplyAsync(() -> {
            Map<String, Object> politicalSpread = null;
            try {
                URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/kvaa/statfin_kvaa_pxt_12xf.px");
                URL url = uri.toURL();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setDoOutput(true);

                String jsonInputString = "{"
                        + "\"query\": ["
                        + "    {\"code\": \"Vaalipiiri ja kunta\", \"selection\": {\"filter\": \"item\", \"values\": [" + getAreaCode(municipality) + "]}},"
                        + "    {\"code\": \"Tiedot\", \"selection\": {\"filter\": \"item\", \"values\": [\"osuus_ehd\"]}},"
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

                JSONObject politicalSpreadListValues = new JSONObject(responseContent.toString());

                JSONArray politicalSpreadList = new JSONObject(responseContent.toString()).getJSONArray("value");

                Gson gson = new Gson();
                ResponseData responseData = gson.fromJson(responseContent.toString(), ResponseData.class);
                Map<String, Object> dimensionMap = responseData.getDimension();

                // puolueNamesAndCodes -- "201": "SDP"
                Map<String, Object> object1 = (Map<String, Object>) dimensionMap.get("Puolue");
                Map<String, Object> object2 = (Map<String, Object>) object1.get("category");
                Map<String, Object> puolueNamesAndCodes = (Map<String, Object>) object2.get("label");

                // puoluePlacement -- "203": 1
                Map<String, Object> object12 = (Map<String, Object>) dimensionMap.get("Puolue");
                Map<String, Object> object22 = (Map<String, Object>) object12.get("category");
                Map<String, Object> puoluePlacement = (Map<String, Object>) object22.get("index");

                // puoluePlacement -- "203": 1
                List<Integer> puolueValues = (List<Integer>) dimensionMap.get("value");

                Map<String, Object> puolueAndPercent = null;

                // make list
                for (int i = 0; i < puolueNamesAndCodes.size(); i++) {
                    Optional<String> puolueCode = getKeyByValue(puoluePlacement, i);
                    String puolueName = puolueNamesAndCodes.get(puolueCode).toString();

                    puolueAndPercent.put(puolueName, puolueNamesAndCodes);
                }

                politicalSpread = puolueAndPercent;

            } catch (Exception e) {
                e.printStackTrace();
                //jsonResponse.put("error", "Error during request: " + e.getMessage());
                System.out.println(e);
            }
            return politicalSpread;
        });
    }
}
