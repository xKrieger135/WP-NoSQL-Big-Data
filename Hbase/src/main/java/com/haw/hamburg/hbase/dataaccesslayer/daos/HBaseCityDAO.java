package com.haw.hamburg.hbase.dataaccesslayer.daos;

import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.*;
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
//            System.out.println(c.getId());
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

    public City getCityNameByPostalcode(Connection connection, String tableName, String columnFamilyName, int postalcode) throws IOException {
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

    public City getPostalcodeByCityName(Connection connection, String tableName, String columnFamilyName, String cityName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);

        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(columnFamilyName),
                Bytes.toBytes("City"), CompareFilter.CompareOp.EQUAL, Bytes.toBytes("HAMBURG"));
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner rs = hTable.getScanner(scan);
        for(Result result : rs) {
            System.out.println(Bytes.toInt(result.getRow()));
        }
        return null;
    }
}
