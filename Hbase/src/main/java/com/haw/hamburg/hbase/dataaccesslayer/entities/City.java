package com.haw.hamburg.hbase.dataaccesslayer.entities;

import java.util.List;

/**
 * Created by nosql on 4/23/16.
 */
public class City {

    private int id;
    private String name;
    private List<Double> location;
    private int population;
    private String state;

    public City(int id, String name, List<Double> location, int population, String state) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.population = population;
        this.state = state;
    }
}
