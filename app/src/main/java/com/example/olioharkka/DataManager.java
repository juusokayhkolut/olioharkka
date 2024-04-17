package com.example.olioharkka;

import java.util.HashMap;

public class DataManager {
    private static DataManager instance;
    private HashMap<String, String> dataMap;

    private DataManager() {
        dataMap = new HashMap<>();
    }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void setData(String key, String value) {
        dataMap.put(key, value);
    }

    public String getData(String key) {
        return dataMap.get(key);
    }

    public void removeData(String key) {
        dataMap.remove(key);
    }

    public void clearAllData() {
        dataMap.clear();
    }
}
