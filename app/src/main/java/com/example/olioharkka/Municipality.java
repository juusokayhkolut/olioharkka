package com.example.olioharkka;

import java.util.Map;

public class Municipality {
    private String name;
    private Integer population;
    private Double populationChange;
    private String wikipediaLink;
    private Double workSelfSufficiency;
    private Double employmentRate;
    private Double summerCottages;
    private Weather weather;

    // Constructor
    public Municipality(String name, Integer population, Double populationChange, String wikipediaLink,
                        Double workSelfSufficiency, Double employmentRate,
                        Double summerCottages, Weather weather) {
        this.name = name;
        this.population = population;
        this.populationChange = populationChange;
        this.wikipediaLink = wikipediaLink;
        this.workSelfSufficiency = workSelfSufficiency;
        this.employmentRate = employmentRate;
        this.summerCottages = summerCottages;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Double getPopulationChange() {
        return populationChange;
    }

    public void setPopulationChange(Double populationChange) {
        this.populationChange = populationChange;
    }

    public String getWikipediaLink() {
        return wikipediaLink;
    }

    public void setPopulationChange(String wikipediaLink) {
        this.wikipediaLink = wikipediaLink;
    }

    public Double getWorkSelfSufficiency() {
        return workSelfSufficiency;
    }

    public void setWorkSelfSufficiency(Double workSelfSufficiency) {
        this.workSelfSufficiency = workSelfSufficiency;
    }

    public Double getEmploymentRate() {
        return employmentRate;
    }

    public void setEmploymentRate(Double employmentRate) {
        this.employmentRate = employmentRate;
    }

    public Double getSummerCottages() {
        return summerCottages;
    }

    public void setSummerCottages(Double summerCottages) {
        this.summerCottages = summerCottages;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
