package com.haw.hamburg.hbase.dataaccesslayer.entities;

import java.util.Map;

/**
 * Created by nosql on 4/23/16.
 */
public class City {

    private String id;
    private String name;
    private Map<Double, Double> location;
    private int population;
    private String state;
    private String footballCity;

    public City(String id, String name, Map<Double, Double> location, int population, String state, String footballCity) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.population = population;
        this.state = state;
        this.footballCity = footballCity;
    }


    public City(String id, String name, Map<Double, Double> location, int population, String state) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.population = population;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Double, Double> getLocation() {
        return location;
    }

    public int getPopulation() {
        return population;
    }

    public String getState() {
        return state;
    }

    public String getFootballCity() {
        return footballCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (getPopulation() != city.getPopulation()) return false;
        if (!getId().equals(city.getId())) return false;
        if (!getName().equals(city.getName())) return false;
        if (!getLocation().equals(city.getLocation())) return false;
        if (!getState().equals(city.getState())) return false;
        return getFootballCity() != null ? getFootballCity().equals(city.getFootballCity()) : city.getFootballCity() == null;

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getLocation().hashCode();
        result = 31 * result + getPopulation();
        result = 31 * result + getState().hashCode();
        result = 31 * result + (getFootballCity() != null ? getFootballCity().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", population=" + population +
                ", state='" + state + '\'' +
                ", footballCity='" + footballCity + '\'' +
                '}';
    }
}
