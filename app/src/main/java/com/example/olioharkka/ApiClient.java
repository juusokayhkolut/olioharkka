package com.example.olioharkka;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.StrictMode;
import android.util.Log;

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
import java.net.MalformedURLException;
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

    public static Integer searchForMunicipalityPopulation(String municipality) {
        Integer population = 0;

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        try {
            URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11ra.px");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString =    "{\n" +
                                        "    \"query\": [\n" +
                                        "        {\n" +
                                        "            \"code\": \"Alue\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"" + CityCodeLookup.getCityCode(municipality) + "\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"code\": \"Tiedot\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"vaesto\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"code\": \"Vuosi\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"2022\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "    ],\n" +
                                        "    \"response\": {\n" +
                                        "        \"format\": \"json-stat2\"\n" +
                                        "    }\n" +
                                        "}";


            try(OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            connection.connect();
            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }

            Gson gson = new Gson();
            ResponseData responseData = gson.fromJson(buffer.toString(), ResponseData.class);

            JSONObject jsonObject = new JSONObject(buffer.toString());
            population = Integer.parseInt(jsonObject.get("value").toString().replace("[", "").replace("]", ""));

            return population;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Map<String, Object> getPoliticalSpread(String municipality) {
        // returns ["KOK": 100, "SDP": 100] etc

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Map<String, Object> politicalSpread = null;
        try {
            URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/kvaa/statfin_kvaa_pxt_12xf.px");
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String jsonInputString =    "{\n" +
                                        "    \"query\": [\n" +
                                        "        {\n" +
                                        "            \"code\": \"Vaalipiiri ja kunta\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"021543\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"code\": \"Ehdokkaan sukupuoli\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"SSS\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"code\": \"Tiedot\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"osuus_ehd\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "    ],\n" +
                                        "    \"response\": {\n" +
                                        "        \"format\": \"json-stat2\"\n" +
                                        "    }\n" +
                                        "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            Map<String, Object> dimensionMap = getStringObjectMap(conn);
            System.out.println("dimensionMap");
            System.out.println(dimensionMap);

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
    }

    private static Map<String, Object> getStringObjectMap(HttpURLConnection conn) throws IOException {
        StringBuilder responseContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
        }

        Gson gson = new Gson();
        ResponseData responseData = gson.fromJson(responseContent.toString(), ResponseData.class);
        Map<String, Object> dimensionMap = responseData.getDimension();
        return dimensionMap;
    }
}
