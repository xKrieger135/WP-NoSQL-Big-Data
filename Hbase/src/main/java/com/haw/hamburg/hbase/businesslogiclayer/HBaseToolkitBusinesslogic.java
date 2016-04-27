package com.haw.hamburg.hbase.businesslogiclayer;

import com.haw.hamburg.hbase.dataaccesslayer.daos.HBaseCityDAO;
import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import com.haw.hamburg.util.filereaderadapter.FileReaderAdapter;
import com.haw.hamburg.util.filereaderadapter.interfaces.IFileReaderAdapter;
import com.haw.hamburg.util.jsonconverteradapter.JSONConverterAdapter;
import com.haw.hamburg.util.jsonconverteradapter.interfaces.IJSONConverterAdapter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nosql on 4/22/16.
 */
public class HBaseToolkitBusinesslogic {

    private Configuration hbaseConfig = null;
    private HBaseAdmin admin = null;
    private HBaseCityDAO hBaseCityDAO = null;

    public HBaseToolkitBusinesslogic() {
        this.hbaseConfig = HBaseConfiguration.create();
        this.hBaseCityDAO = new HBaseCityDAO();
    }

    public void insert(List<JSONObject> importData, String tableName, String columnFamilyName) throws IOException, JSONException {
        admin = new HBaseAdmin(hbaseConfig);
        createTable(hbaseConfig, tableName, columnFamilyName);
        for (JSONObject element : importData) {
            Map<Double, Double> locationCoordinates = new HashMap<>();
            JSONArray coordinates = element.getJSONArray("loc");
            locationCoordinates.put(coordinates.getDouble(0), coordinates.getDouble(1));

            City city = new City(element.getInt("_id"), element.getString("city"), locationCoordinates, element.getInt("pop"), element.getString("state"));
            hBaseCityDAO.dataImport(hbaseConfig, city, tableName);
        }
    }

    private void createTable(Configuration config, String tableName, String columnFamilyName) throws IOException {
        HTableDescriptor table = null;
        if(config != null && !(tableName.isEmpty())) {
            table = new HTableDescriptor(TableName.valueOf(tableName));
            addColumnFamily(table, columnFamilyName);
            if(!admin.tableExists(tableName)) {
                admin.createTable(table);
                closeConnection();
            }
        }
    }

    private void addColumnFamily(HTableDescriptor table, String columnFamilyName) {
        table.addFamily(new HColumnDescriptor(columnFamilyName));
    }

    private Connection getServerConnection() throws IOException {
        Connection connection = ConnectionFactory.createConnection(hbaseConfig);
        return connection;
    }

    private void closeConnection() {
        try {
            admin.close();
        } catch (IOException e) {
            throw new RuntimeException("While closing the connection went something wrong!", e);
        }
    }

    public static void main(String[] args) {
        HBaseToolkitBusinesslogic x = new HBaseToolkitBusinesslogic();
        IFileReaderAdapter f = new FileReaderAdapter();
        IJSONConverterAdapter json = new JSONConverterAdapter();
        List<String> l = null;
        List<JSONObject> j = null;
        System.out.println("OK");
        try {
            l = f.readFile("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/plz.data");
            j = json.convertToJSONList(l);
            System.out.println("OK AGAIN");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            x.insert(j, "abcabc", "bcabca");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
