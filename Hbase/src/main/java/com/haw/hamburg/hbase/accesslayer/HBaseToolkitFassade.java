package com.haw.hamburg.hbase.accesslayer;

import com.haw.hamburg.hbase.businesslogiclayer.HBaseToolkitBusinesslogic;
import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 4/22/16.
 */
@Component
public class HBaseToolkitFassade {

    private HBaseToolkitBusinesslogic hBaseToolkitBusinesslogic = null;
    private List<JSONObject> importData = null;

    public HBaseToolkitFassade(List<JSONObject> importData) {
        this.hBaseToolkitBusinesslogic = new HBaseToolkitBusinesslogic();
        this.importData = importData;
    }

    public HBaseToolkitFassade() {
        this.hBaseToolkitBusinesslogic = new HBaseToolkitBusinesslogic();
    }

    public void databaseImport(List<JSONObject> importData, String table, String columnFamily) throws IOException, JSONException {
        if(importData != null && !(table.isEmpty()) && !(columnFamily.isEmpty())) {
            hBaseToolkitBusinesslogic.databaseImport(importData, table, columnFamily);
        }
    }

    public JSONObject searchCityByPostalcode(String tableName, String columnFamilyName, String postalcode) throws IOException, JSONException {
        City city = hBaseToolkitBusinesslogic.getCityNameByPostalcode(tableName, columnFamilyName, postalcode);
        JSONObject cityAsJSON = new JSONObject();

        cityAsJSON.put("State", city.getState());
        cityAsJSON.put("Location", city.getLocation());
        cityAsJSON.put("Population", city.getPopulation());
        cityAsJSON.put("City", city.getName());
        cityAsJSON.put("Postalcode", city.getId());

//        // Add this only if the footballcity attribute contains a String "ja"
        if(city.getFootballCity() != null) {
            cityAsJSON.put("FootballCity", city.getFootballCity());
        }
        return  cityAsJSON;
    }

    public JSONObject searchPostalcodeByCity(String tableName, String columnFamilyName, String city) throws IOException, JSONException {
        List<String> postalcodes = hBaseToolkitBusinesslogic.getPostalcodeByCityName(tableName, columnFamilyName, city);
        JSONObject postalcodesAsJson = new JSONObject();
        postalcodesAsJson.put("Postalcodes", postalcodes);
        return postalcodesAsJson;
    }

    public void createTable(String table, String columnFamily) throws IOException {
        if(!(table.isEmpty())) {
            hBaseToolkitBusinesslogic.createTable(table, columnFamily);
        }
    }

    public void addColumnFamily(String table, String columnFamily) throws IOException {
        if (!table.isEmpty() && !columnFamily.isEmpty()) {
            hBaseToolkitBusinesslogic.addColumnFamily(table, columnFamily);
        }
    }

    public void dropTable(String table) throws IOException {
        if (!table.isEmpty()) {
            hBaseToolkitBusinesslogic.dropTable(table);
        }
    }

    public void addColumnFamilyValue(String table, String columnFamily, String rowIdentifier, String value) throws IOException {
        if(!table.isEmpty() && !columnFamily.isEmpty() && !value.isEmpty()) {
            hBaseToolkitBusinesslogic.addColumnFamilyValue(table, columnFamily, rowIdentifier, value);
        }
    }
}
