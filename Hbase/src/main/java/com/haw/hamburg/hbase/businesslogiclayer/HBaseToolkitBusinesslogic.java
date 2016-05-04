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
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nosql on 4/22/16.
 */
public class HBaseToolkitBusinesslogic {

    private Configuration hbaseConfig = null;
    private HBaseCityDAO hBaseCityDAO = null;

    private final String ZIP = "_id";
    private final String CITY = "city";
    private final String POPULATION = "pop";
    private final String LOCATION = "loc";
    private final String STATE = "state";

    public HBaseToolkitBusinesslogic() {
        this.hbaseConfig = HBaseConfiguration.create();
        this.hBaseCityDAO = new HBaseCityDAO();
    }

    public void databaseImport(List<JSONObject> importData, String tableName, String columnFamilyName) throws IOException, JSONException {
        createTable(hbaseConfig, tableName, columnFamilyName);
        Connection connection = getServerConnection();
        List<City> cities = new ArrayList<>();
        for (JSONObject element : importData) {
            Map<Double, Double> locationCoordinates = new HashMap<>();
            JSONArray coordinates = element.getJSONArray(LOCATION);
            locationCoordinates.put(coordinates.getDouble(0), coordinates.getDouble(1));

            City city = new City(element.getString(ZIP), element.getString(CITY), locationCoordinates, element.getInt(POPULATION), element.getString(STATE));
            cities.add(city);
        }
        hBaseCityDAO.databaseImport(cities, connection, tableName, columnFamilyName);
        closeConnection(connection);
    }

    public City getCityNameByPostalcode(String tableName, String columnFamilyName, String postalcode) throws IOException {
        Connection connection = getServerConnection();
        City city = hBaseCityDAO.getCityNameByPostalcode(connection, tableName, columnFamilyName, postalcode);
        return city;
    }

    public List<String> getPostalcodeByCityName(String tableName, String columnFamilyName, String cityName) throws IOException {
        Connection connection = getServerConnection();
        List<City> cities = hBaseCityDAO.getPostalcodeByCityName(connection, tableName, columnFamilyName, cityName);
        List<String> postalcodes = new ArrayList<>();
        for(City city : cities) {
            postalcodes.add(city.getId());
        }
        return postalcodes;
    }

    private void createTable(Configuration config, String tableName, String columnFamilyName) throws IOException {
        Connection connection = getServerConnection();
        TableName tName = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        HTableDescriptor table = null;
        if(config != null && !(tableName.isEmpty())) {
            table = new HTableDescriptor(tName);
            addColumnFamily(table, columnFamilyName);
            if(!admin.tableExists(tName)) {
                admin.createTable(table);
                closeConnection(connection);
            }
        }
    }

    private void addColumnFamily(HTableDescriptor table, String columnFamilyName) {
        table.addFamily(new HColumnDescriptor(columnFamilyName));
    }

    /**
     * This method will get the HBase connection.
     * @return                Returns the HBase connection as connection object.
     * @throws IOException    This exception will be thrown, when something went wrong while creating the connection.
     */
    private Connection getServerConnection() throws IOException {
        Connection connection = ConnectionFactory.createConnection(hbaseConfig);
        return connection;
    }

    /**
     * This method will close the connection to HBase.
     * @param connection    This is the connection, to get access to HBase.
     */
    private void closeConnection(Connection connection) {
        try {
            connection.getAdmin().close();
        } catch (IOException e) {
            throw new RuntimeException("While closing the connection went something wrong!", e);
        }
    }

//    public static void main(String[] args) {
//        HBaseToolkitBusinesslogic x = new HBaseToolkitBusinesslogic();
//        IFileReaderAdapter f = new FileReaderAdapter();
//        IJSONConverterAdapter json = new JSONConverterAdapter();
//        List<String> l = null;
//        List<JSONObject> j = null;
//        System.out.println("OK");
//        try {
//            l = f.readFile("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/plz.data");
//            j = json.convertToJSONList(l);
//            System.out.println("OK AGAIN");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            x.databaseImport(j, "BigData", "CityData");
//            x.getCityNameByPostalcode("BigData", "CityData", "99950");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
