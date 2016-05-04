package com.haw.hamburg.hbase.accesslayer;

import com.haw.hamburg.hbase.businesslogiclayer.HBaseToolkitBusinesslogic;
import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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

    public void databaseImport(List<JSONObject> importData, String tableName, String columnFamilyName) throws IOException, JSONException {
        if(importData != null && !(tableName.equals("")) && !(columnFamilyName.equals(""))) {
            hBaseToolkitBusinesslogic.databaseImport(importData, tableName, columnFamilyName);
        }
    }

    public JSONObject searchCityByPostalcode(String tableName, String columnFamilyName, String postalcode) throws IOException, JSONException {
        City city = hBaseToolkitBusinesslogic.getCityNameByPostalcode(tableName, columnFamilyName, postalcode);
        JSONObject cityAsJSON = new JSONObject();
        cityAsJSON.append("Postalcode", city.getId());
        cityAsJSON.append("City", city.getName());
        cityAsJSON.append("Population", city.getPopulation());
        cityAsJSON.append("Location" ,new JSONObject(city.getLocation()));
        return  cityAsJSON;
    }

    public JSONObject searchPostalcodeByCity(String tableName, String columnFamilyName, String city) throws IOException {
        List<String> postalcodes = hBaseToolkitBusinesslogic.getPostalcodeByCityName(tableName, columnFamilyName, city);
        JSONObject postalcodesAsJson = new JSONObject(postalcodes);
        return postalcodesAsJson;
    }
}
