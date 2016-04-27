package com.haw.hamburg.hbase.dataaccesslayer.entities;

import java.util.Map;

/**
 * Created by nosql on 4/23/16.
 */
public class City {

    private int id;
    private String name;
    private Map<Double, Double> location;
    private int population;
    private String state;

    public City(int id, String name, Map<Double, Double> location, int population, String state) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.population = population;
        this.state = state;
    }

    public int getId() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (getId() != city.getId()) return false;
        if (getPopulation() != city.getPopulation()) return false;
        if (!getName().equals(city.getName())) return false;
        if (!getLocation().equals(city.getLocation())) return false;
        return getState().equals(city.getState());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getLocation().hashCode();
        result = 31 * result + getPopulation();
        result = 31 * result + getState().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", population=" + population +
                ", state='" + state + '\'' +
                '}';
    }
}
