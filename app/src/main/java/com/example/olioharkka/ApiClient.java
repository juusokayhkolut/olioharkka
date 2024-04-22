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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

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

    public static Double getPopulationChange(String municipality) {
        // returns ["KOK": 100, "SDP": 100] etc
        // ei toimi
        BufferedReader reader = null;

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Double populationChange = 0.0;
        try {
            URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            String jsonInputString =    "{\n" +
                                        "    \"query\": [\n" +
                                        "        {\n" +
                                        "            \"code\": \"Alue\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \""+CityCodeLookup.getCityCode(municipality)+"\"\n" +
                                        "                ]\n" +
                                        "            }\n" +
                                        "        },\n" +
                                        "        {\n" +
                                        "            \"code\": \"Tiedot\",\n" +
                                        "            \"selection\": {\n" +
                                        "                \"filter\": \"item\",\n" +
                                        "                \"values\": [\n" +
                                        "                    \"vm01\"\n" +
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
                                        "        },\n" +
                                        "    ],\n" +
                                        "    \"response\": {\n" +
                                        "        \"format\": \"json-stat2\"\n" +
                                        "    }\n" +
                                        "}";


            try (OutputStream os = connection.getOutputStream()) {
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
            Map<String, Object> dimensionMap = responseData.getDimension();

            // Labels
            Map<String, Object> object1 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object2 = (Map<String, Object>) object1.get("category");
            Map<String, Object> labels = (Map<String, Object>) object2.get("label");

            // Index
            Map<String, Object> object12 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object22 = (Map<String, Object>) object12.get("category");
            Map<String, Integer> index = (Map<String, Integer>) object22.get("index");

            // Values
            JSONObject jsonObject = new JSONObject(buffer.toString());
            List<Double> values = new ArrayList<Double>();

            for (String value: jsonObject.get("value").toString().replace("[", "").replace("]", "").split(",")) {
                values.add(Double.parseDouble(value));
            }

            Map<String, Object> populationChanges = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                populationChanges.put(labels.get(index.keySet().toArray()[i].toString()).toString(), values.get(i));
            }

            populationChange = Double.parseDouble(populationChanges.get("2022").toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return populationChange;
    }

    public static Double getWorkSelfSufficiency(String municipality) {
        BufferedReader reader = null;

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Double workSelfSufficiency = 0.0;
        try {
            URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            String jsonInputString =    "{\n" +
                    "    \"query\": [\n" +
                    "        {\n" +
                    "            \"code\": \"Alue\",\n" +
                    "            \"selection\": {\n" +
                    "                \"filter\": \"item\",\n" +
                    "                \"values\": [\n" +
                    "                    \""+CityCodeLookup.getCityCode(municipality)+"\"\n" +
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
                    "        },\n" +
                    "    ],\n" +
                    "    \"response\": {\n" +
                    "        \"format\": \"json-stat2\"\n" +
                    "    }\n" +
                    "}";


            try (OutputStream os = connection.getOutputStream()) {
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
            Map<String, Object> dimensionMap = responseData.getDimension();

            // Labels
            Map<String, Object> object1 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object2 = (Map<String, Object>) object1.get("category");
            Map<String, Object> labels = (Map<String, Object>) object2.get("label");

            // Index
            Map<String, Object> object12 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object22 = (Map<String, Object>) object12.get("category");
            Map<String, Integer> index = (Map<String, Integer>) object22.get("index");

            // Values
            JSONObject jsonObject = new JSONObject(buffer.toString());
            List<Double> values = new ArrayList<Double>();

            for (String value: jsonObject.get("value").toString().replace("[", "").replace("]", "").split(",")) {
                values.add(Double.parseDouble(value));
            }

            Map<String, Object> workSelfSufficiencies = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                workSelfSufficiencies.put(labels.get(index.keySet().toArray()[i].toString()).toString(), values.get(i));
            }

            workSelfSufficiency = Double.parseDouble(workSelfSufficiencies.get("2022").toString());
        } catch (Exception e) {
            e.printStackTrace();
            //jsonResponse.put("error", "Error during request: " + e.getMessage());
            System.out.println(e);
        }
        return workSelfSufficiency;
    }

    public static Double getEmploymentRate(String municipality) {
        BufferedReader reader = null;

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Double employmentRate = 0.0;
        try {
            URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            String jsonInputString =    "{\n" +
                    "    \"query\": [\n" +
                    "        {\n" +
                    "            \"code\": \"Alue\",\n" +
                    "            \"selection\": {\n" +
                    "                \"filter\": \"item\",\n" +
                    "                \"values\": [\n" +
                    "                    \""+CityCodeLookup.getCityCode(municipality)+"\"\n" +
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
                    "        },\n" +
                    "        {\n" +
                    "            \"code\": \"Tiedot\",\n" +
                    "            \"selection\": {\n" +
                    "                \"filter\": \"item\",\n" +
                    "                \"values\": [\n" +
                    "                    \"tyollisyysaste\"\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"response\": {\n" +
                    "        \"format\": \"json-stat2\"\n" +
                    "    }\n" +
                    "}";


            try (OutputStream os = connection.getOutputStream()) {
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
            Map<String, Object> dimensionMap = responseData.getDimension();

            // Labels
            Map<String, Object> object1 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object2 = (Map<String, Object>) object1.get("category");
            Map<String, Object> labels = (Map<String, Object>) object2.get("label");

            // Index
            Map<String, Object> object12 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object22 = (Map<String, Object>) object12.get("category");
            Map<String, Integer> index = (Map<String, Integer>) object22.get("index");

            // Values
            JSONObject jsonObject = new JSONObject(buffer.toString());
            List<Double> values = new ArrayList<Double>();

            for (String value: jsonObject.get("value").toString().replace("[", "").replace("]", "").split(",")) {
                values.add(Double.parseDouble(value));
            }

            Map<String, Object> employmentRates = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                employmentRates.put(labels.get(index.keySet().toArray()[i].toString()).toString(), values.get(i));
            }

            employmentRate = Double.parseDouble(employmentRates.get("2022").toString());
        } catch (Exception e) {
            e.printStackTrace();
            //jsonResponse.put("error", "Error during request: " + e.getMessage());
            System.out.println(e);
        }
        return employmentRate;
    }

    public static Double getAmountOfSummerCottages(String municipality) {
        BufferedReader reader = null;

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        Double amountOfSummerCottages = 0.0;
        try {
            URI uri = new URI("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/rakke/statfin_rakke_pxt_116j.px");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setDoOutput(true);

            String jsonInputString =    "{\n" +
                    "    \"query\": [\n" +
                    "        {\n" +
                    "            \"code\": \"Alue\",\n" +
                    "            \"selection\": {\n" +
                    "                \"filter\": \"item\",\n" +
                    "                \"values\": [\n" +
                    "                    \""+CityCodeLookup.getCityCode(municipality)+"\"\n" +
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


            try (OutputStream os = connection.getOutputStream()) {
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
            Map<String, Object> dimensionMap = responseData.getDimension();

            // Labels
            Map<String, Object> object1 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object2 = (Map<String, Object>) object1.get("category");
            Map<String, Object> labels = (Map<String, Object>) object2.get("label");

            // Index
            Map<String, Object> object12 = (Map<String, Object>) dimensionMap.get("Vuosi");
            Map<String, Object> object22 = (Map<String, Object>) object12.get("category");
            Map<String, Integer> index = (Map<String, Integer>) object22.get("index");

            // Values
            JSONObject jsonObject = new JSONObject(buffer.toString());
            List<Double> values = new ArrayList<Double>();

            for (String value: jsonObject.get("value").toString().replace("[", "").replace("]", "").split(",")) {
                values.add(Double.parseDouble(value));
            }

            Map<String, Object> amountOfSummerCottagesList = new HashMap<>();

            for (int i = 0; i < values.size(); i++) {
                amountOfSummerCottagesList.put(labels.get(index.keySet().toArray()[i].toString()).toString(), values.get(i));
            }

            amountOfSummerCottages = Double.parseDouble(amountOfSummerCottagesList.get("2022").toString());
        } catch (Exception e) {
            e.printStackTrace();
            //jsonResponse.put("error", "Error during request: " + e.getMessage());
            System.out.println(e);
        }
        return amountOfSummerCottages;
    }

    public static Map<String, Object> getWeather(String municipality) {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        URL url = null;

        Map<String, Object> weatherMap = new HashMap();
        try {
            Map<String, Object> lonLat = getLonLatForCity(municipality);

            url = new URL("https://api.openweathermap.org/data/3.0/onecall?lat="+lonLat.get("lat")+"&lon="+lonLat.get("lon")+"&appid=5bbb818d5bc8ba509e8c1c20284c8e95\n");
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }

            Map<String, Object> retMap = new Gson().fromJson(
                    buffer.toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
            );

            weatherMap = (Map) retMap.get("current");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return weatherMap;
    };

    public static Map<String, Object> getLonLatForCity(String municipality) {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        URL url = null;

        Map<String, Object> lonLatMap = new HashMap();
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+municipality+"&appid=5bbb818d5bc8ba509e8c1c20284c8e95\n");
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
            }

            Map<String, Object> retMap = new Gson().fromJson(
                    buffer.toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
            );

            Double lon = getNestedValue(retMap, "coord", "lon");
            Double lat = getNestedValue(retMap, "coord", "lat");

            lonLatMap.put("lon", lon);
            lonLatMap.put("lat", lat);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lonLatMap;
    };

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

    public static <T> T getNestedValue(Map map, String... keys) {
        Object value = map;

        for (String key : keys) {
            value = ((Map) value).get(key);
        }

        return (T) value;
    }
}
