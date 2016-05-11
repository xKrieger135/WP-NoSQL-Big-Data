package com.haw.hamburg.hbase.dataaccesslayer.daos;

import com.google.common.collect.Iterables;
import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.*;

/**
 * Created by Krieger135 on 4/22/16.
 */
public class HBaseCityDAO {

    // Columnfamily Columns
    private final String CITY = "City";
    private final String LOCATION = "Location";
    private final String POPULATION = "Population";
    private final String STATE = "State";
    private final String FOOTBALLCITY = "footballCity";

    // Columnfamilies
    private final String CITY_DATA = "CityData";
    private final String FUSSBALL = "fussball";

    // Values
    private final String HAMBURG = "HAMBURG";
    private final String BREMEN = "BREMEN";

    public HBaseCityDAO() {

    }

    /**
     * This method imports data into the HBase DB.
     *
     * @param cities              The cities, which should be imported into the database.
     * @param connection          This is the connection, to get access to the HBase database.
     * @param tableName           This is the tableName, where this data should be imported.
     * @param columnFamilyName    This is the columnFamily, at the given tableName, where data should be imported.
     * @throws IOException        This Exception will be thrown, when something went wrong, while importing the data.
     */
    public void databaseImport(List<City> cities, Connection connection, String tableName, String columnFamilyName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);
        for (City c :cities) {
            Put hbase = new Put(Bytes.toBytes(c.getId()));
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(CITY), Bytes.toBytes(c.getName()));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(c.getLocation());
            byte[] locationCoordinates = byteArrayOutputStream.toByteArray();

            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(LOCATION), locationCoordinates);
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(POPULATION), Bytes.toBytes(c.getPopulation()));
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes(STATE), Bytes.toBytes(c.getState()));
            hTable.put(hbase);
        }
    }

    /**
     * This method gets one city, for the given postalcode and will return all of the city data.
     *
     * @param connection         This is the connection, to get access to the HBase database.
     * @param tableName          This is the tableName, where this data should be imported.
     * @param columnFamilyName   This is the columnFamily, at the given tableName, where data should be imported.
     * @param postalcode         This is the given postalcode, for which the city will be searched.
     * @return                   Returns the entire city for the given postalcode of the city.
     * @throws IOException       This Exception will be thrown, when something went wrong, while importing the data.
     */
    public City getCityNameByPostalcode(Connection connection, String tableName, String columnFamilyName, String postalcode) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);

        Get get = new Get(Bytes.toBytes(postalcode));
        Result result = hTable.get(get);

        byte[] cityBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes(CITY));
        byte[] populationBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes(POPULATION));
        byte[] stateBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes(STATE));
        byte[] locationBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes(LOCATION));
        byte[] footballCityBytes = result.getValue(Bytes.toBytes(FUSSBALL), Bytes.toBytes(FOOTBALLCITY));

        String cityName = Bytes.toString(cityBytes);
        String state = Bytes.toString(stateBytes);
        int population = Bytes.toInt(populationBytes);
        String footballCity = Bytes.toString(footballCityBytes);

        ByteArrayInputStream b = new ByteArrayInputStream(locationBytes);
        ObjectInputStream o = new ObjectInputStream(b);
        Map<Double, Double> locationCoordinates = new HashMap<>();
        try {
            locationCoordinates = (Map<Double, Double>) o.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        City city = new City(postalcode, cityName, locationCoordinates, population, state, footballCity);

        return city;
    }

    /**
     * This method gets all postalcodes for one given city.
     *
     * @param connection         This is the connection, to get access to the HBase database.
     * @param tableName          This is the tableName, where this data should be imported.
     * @param columnFamilyName   This is the columnFamily, at the given tableName, where data should be imported.
     * @param cityName           This is the given city, for which the postalcodes will be searched.
     * @return                   Returns a list with all city data. The result is stored into a list, because of the
     *                           possibility of multiple postalcodes for one city.
     * @throws IOException       This Exception will be thrown, when something went wrong, while importing the data.
     */
    public List<City> getPostalcodeByCityName(Connection connection, String tableName, String columnFamilyName, String cityName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);

        Filter f = new SingleColumnValueFilter(Bytes.toBytes(columnFamilyName),
                                Bytes.toBytes(CITY), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(cityName));
        Scan scan = new Scan();
        scan.setFilter(f);
        ResultScanner rs = hTable.getScanner(scan);
        List<City> cities = new ArrayList<>();
        for(Result result : rs) {
            String id = Bytes.toString(result.getRow());
            City city = getCityNameByPostalcode(connection, tableName, columnFamilyName, id);
            cities.add(city);
        }
        return cities;
    }

    /**
     * This method will add values to the given table and columnFamily. The special row will be searched by rowIdentifier.
     * At the moment the rowIdentifier is hard coded for a special use case.
     *
     * @param connection     This is the connection, to get access to the HBase database.
     * @param table          This is the tableName, where this data should be imported.
     * @param columnFamily   This is the columnFamily, at the given tableName, where data should be imported.
     * @param rowIdentifier  This is the special identifier for the row, where the value will be inserted.
     * @param value          This is the value, which should be inserted.
     * @throws IOException   This Exception will be thrown, when something went wrong, while inserting the data.
     */
    public void addColumnFamilyValue(Connection connection, String table, String columnFamily, String rowIdentifier, String value) throws IOException {
        TableName tableName = TableName.valueOf(table);
        Table hTable = null;
        if (connection.getAdmin().tableExists(tableName)) {
            hTable = connection.getTable(tableName);
        }

        Filter filterHamburg = new SingleColumnValueFilter(Bytes.toBytes(CITY_DATA),
                Bytes.toBytes(CITY), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(HAMBURG));
        Filter filterBremen = new SingleColumnValueFilter(Bytes.toBytes(CITY_DATA),
                Bytes.toBytes(CITY), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(BREMEN));

        Scan scanHamburg = new Scan();
        Scan scanBremen = new Scan();
        scanHamburg.setFilter(filterHamburg);
        scanBremen.setFilter(filterBremen);
        ResultScanner rsh = hTable.getScanner(scanHamburg);
        ResultScanner rsb = hTable.getScanner(scanBremen);
        List<City> cities = new ArrayList<>();
        for(Result result : Iterables.concat(rsh, rsb)) {
            String id = Bytes.toString(result.getRow());
            City city = getCityNameByPostalcode(connection, table, CITY_DATA, id);
            cities.add(city);
        }

        for(City city : cities) {
            Put put = new Put(Bytes.toBytes(city.getId()));
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(FOOTBALLCITY), Bytes.toBytes(value));
            hTable.put(put);
        }
    }
}
