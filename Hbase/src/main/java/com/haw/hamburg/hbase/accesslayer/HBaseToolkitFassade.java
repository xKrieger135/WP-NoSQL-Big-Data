package com.haw.hamburg.hbase.accesslayer;

import com.haw.hamburg.hbase.businesslogiclayer.HBaseToolkitBusinesslogic;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 4/22/16.
 */
public class HBaseToolkitFassade {

    private HBaseToolkitBusinesslogic hBaseToolkitBusinesslogic = null;
    private List<JSONObject> importData = null;

    public HBaseToolkitFassade(List<JSONObject> importData) {
        this.hBaseToolkitBusinesslogic = new HBaseToolkitBusinesslogic();
        this.importData = importData;
    }

    public void databaseImport(String tableName, String columnFamilyName) throws IOException, JSONException {
        if(importData != null && !(tableName.equals("")) && columnFamilyName.equals("")) {
            hBaseToolkitBusinesslogic.databaseImport(importData, tableName, columnFamilyName);
        }
    }
}
