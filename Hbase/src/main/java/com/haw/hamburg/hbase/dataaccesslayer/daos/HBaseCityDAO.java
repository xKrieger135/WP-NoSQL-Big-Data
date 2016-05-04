package com.haw.hamburg.hbase.dataaccesslayer.daos;

import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krieger135 on 4/22/16.
 */
public class HBaseCityDAO {

    public HBaseCityDAO() {

    }

    public void databaseImport(List<City> cities, Connection connection, String tableName, String columnFamilyName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);
        for (City c :cities) {
            Put hbase = new Put(Bytes.toBytes(c.getId()));
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes("City"), Bytes.toBytes(c.getName()));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(c.getLocation());
            byte[] locationCoordinates = byteArrayOutputStream.toByteArray();

            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes("Location"), locationCoordinates);
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes("Population"), Bytes.toBytes(c.getName()));
            hbase.addColumn(Bytes.toBytes(columnFamilyName), Bytes.toBytes("State"), Bytes.toBytes(c.getName()));
            hTable.put(hbase);
        }
    }

    public City getCityNameByPostalcode(Connection connection, String tableName, String columnFamilyName, String postalcode) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);

        Get get = new Get(Bytes.toBytes(postalcode));
        Result result = hTable.get(get);

        byte[] cityBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes("City"));
        byte[] populationBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes("Population"));
        byte[] stateBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes("State"));
        byte[] locationBytes = result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes("Location"));

        String cityName = Bytes.toString(cityBytes);
        String state = Bytes.toString(stateBytes);
        int population = Bytes.toInt(populationBytes);

        ByteArrayInputStream b = new ByteArrayInputStream(locationBytes);
        ObjectInputStream o = new ObjectInputStream(b);
        Map<Double, Double> locationCoordinates = new HashMap<>();
        try {
            locationCoordinates = (Map<Double, Double>) o.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        City city = new City(postalcode, cityName, locationCoordinates, population, state);

        return city;
    }

    public List<City> getPostalcodeByCityName(Connection connection, String tableName, String columnFamilyName, String cityName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);

        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(tableName), Bytes.toBytes("City"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes(cityName));
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner rs = hTable.getScanner(scan);
        List<City> cities = new ArrayList<>();
        for(Result result : rs) {
            // TODO: Get values to put them into the city objects!
            String id = Bytes.toString(result.getRow());
            String name = Bytes.toString(result.value());
            String state = Bytes.toString(result.getValue(Bytes.toBytes(columnFamilyName), Bytes.toBytes("State")));
            System.out.println(id);
            System.out.println(name);
            System.out.println();
//            System.out.println(state);
        }
        return null;
    }
}
