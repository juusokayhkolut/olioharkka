package com.example.olioharkka;

import org.json.JSONObject;

public class MunicipalityDetails {
    private String name;
    private int population;
    private String wikipedia;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public MunicipalityDetails(String name, int population, String wikipedia) {
        this.name = name;
        this.population = population;
        this.wikipedia = wikipedia;
    }
}
