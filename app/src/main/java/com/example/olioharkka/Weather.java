package com.example.olioharkka;

public class Weather {
    // in Kelvin
    private Double temperature;
    // in m/s
    private Double windSpeed;

    // Constructor
    public Weather(Double temperature, Double windSpeed) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }
}
