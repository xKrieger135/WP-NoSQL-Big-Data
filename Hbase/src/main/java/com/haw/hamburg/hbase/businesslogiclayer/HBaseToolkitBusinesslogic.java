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
import org.apache.hadoop.hbase.client.Table;
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

    /**
     *
     * @param importData
     * @param table
     * @param columnFamilyName
     * @throws IOException
     * @throws JSONException
     */
    public void databaseImport(List<JSONObject> importData, String table, String columnFamilyName) throws IOException, JSONException {
        Connection connection = getServerConnection();

        if(!(connection.getAdmin().tableExists(TableName.valueOf(table)))) {
            createTable(table, columnFamilyName);
        }

        List<City> cities = new ArrayList<>();
        for (JSONObject element : importData) {
            Map<Double, Double> locationCoordinates = new HashMap<>();
            JSONArray coordinates = element.getJSONArray(LOCATION);
            locationCoordinates.put(coordinates.getDouble(0), coordinates.getDouble(1));

            City city = new City(element.getString(ZIP), element.getString(CITY), locationCoordinates, element.getInt(POPULATION), element.getString(STATE));
            cities.add(city);
        }
        hBaseCityDAO.databaseImport(cities, connection, table, columnFamilyName);
        closeConnection(connection);
    }

    /**
     *
     * @param table
     * @param columnFamilyName
     * @param postalcode
     * @return
     * @throws IOException
     */
    public City getCityNameByPostalcode(String table, String columnFamilyName, String postalcode) throws IOException {
        Connection connection = getServerConnection();
        City city = hBaseCityDAO.getCityNameByPostalcode(connection, table, columnFamilyName, postalcode);
        return city;
    }

    /**
     *
     * @param table
     * @param columnFamilyName
     * @param cityName
     * @return
     * @throws IOException
     */
    public List<String> getPostalcodeByCityName(String table, String columnFamilyName, String cityName) throws IOException {
        Connection connection = getServerConnection();
        List<City> cities = hBaseCityDAO.getPostalcodeByCityName(connection, table, columnFamilyName, cityName);
        List<String> postalcodes = new ArrayList<>();
        for(City city : cities) {
            postalcodes.add(city.getId());
        }
        return postalcodes;
    }

    /**
     * This method creates one HBaseTable. For HBase Tables there must be at least one column family for creation.
     *
     * @param tableName
     * @param columnFamily
     * @throws IOException
     */
    public void createTable(String tableName, String columnFamily) throws IOException {
        Connection connection = getServerConnection();
        TableName tName = TableName.valueOf(tableName);
        Admin admin = connection.getAdmin();
        HTableDescriptor hTable = new HTableDescriptor(tName);
            if(!admin.tableExists(tName)) {
                addColumnFamily(hTable, columnFamily);
                admin.createTable(hTable);
                closeConnection(connection);
            }
    }

    /**
     * This method will add a columnFamily to the given HBaseTable. Please notice, that this method is only to use,
     * while table creation, because tables need at least one column family.
     *
     * @param hTable
     * @param columnFamily
     */
    private void addColumnFamily(HTableDescriptor hTable, String columnFamily) {
        hTable.addFamily(new HColumnDescriptor(columnFamily));
    }

    /**
     * This method will add column families to an existing table.
     *
     * @param table
     * @param columnFamilyName
     * @throws IOException
     */
    public void addColumnFamily(String table, String columnFamilyName) throws IOException {
        Connection connection = getServerConnection();
        TableName tName = TableName.valueOf(table);
        Admin admin = connection.getAdmin();
        if (admin.tableExists(tName)) {
            admin.addColumn(tName, new HColumnDescriptor(columnFamilyName));
            closeConnection(connection);
        }
    }

    public void dropTable(String table) throws IOException {
        Connection connection = getServerConnection();
        TableName tableName = TableName.valueOf(table);
        Admin admin = connection.getAdmin();
        admin.disableTable(tableName);
        admin.deleteTable(tableName);
    }

    public void addColumnFamilyValue(String table, String columnFamily, String value) throws IOException {
        Connection connection = getServerConnection();
        hBaseCityDAO.addColumnFamilyValue(connection, table, columnFamily, value);
    }

    /**
     * This method will get the HBase connection.
     *
     * @return                Returns the HBase connection as connection object.
     * @throws IOException    This exception will be thrown, when something went wrong while creating the connection.
     */
    private Connection getServerConnection() throws IOException {
        Connection connection = ConnectionFactory.createConnection(hbaseConfig);
        return connection;
    }

    /**
     * This method will close the connection to HBase.
     *
     * @param connection    This is the connection, to get access to HBase.
     */
    private void closeConnection(Connection connection) {
        try {
            connection.getAdmin().close();
        } catch (IOException e) {
            throw new RuntimeException("While closing the connection went something wrong!", e);
        }
    }
}
