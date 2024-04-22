package com.example.olioharkka;

import java.util.Map;

public class Municipality {
    private String name;
    private Integer population;
    private Double populationChange;
    private Double workSelfSufficiency;
    private Double employmentRate;
    private Double summerCottages;
    private Map<String, Object> weather;

    // Constructor
    public Municipality(String name, Integer population, Double populationChange,
                        Double workSelfSufficiency, Double employmentRate,
                        Double summerCottages, Map<String, Object> weather) {
        this.name = name;
        this.population = population;
        this.populationChange = populationChange;
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

    public Map<String, Object> getWeather() {
        return weather;
    }

    public void setWeather(Map<String, Object> weather) {
        this.weather = weather;
    }
}
