package com.example.olioharkka;

public class MunicipalityDetails {
    private String name;
    private int population;
    private double latitude;
    private double longitude;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MunicipalityDetails(String name, int population, double latitude, double longitude) {
        this.name = name;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
