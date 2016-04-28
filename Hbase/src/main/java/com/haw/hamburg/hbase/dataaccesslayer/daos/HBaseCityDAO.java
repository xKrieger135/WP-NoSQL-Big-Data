package com.haw.hamburg.hbase.dataaccesslayer.daos;

import com.haw.hamburg.hbase.dataaccesslayer.entities.City;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Krieger135 on 4/22/16.
 */
public class HBaseCityDAO {

    public HBaseCityDAO() {

    }

    public void databaseImport(List<City> cities, Connection connection, String tableName) throws IOException {
        TableName tName = TableName.valueOf(tableName);
        Table hTable = connection.getTable(tName);
        for (City c :cities) {
            Put hbase = new Put(Bytes.toBytes(c.getId()));
            hbase.addColumn(Bytes.toBytes("bcabca"), Bytes.toBytes("City"), Bytes.toBytes(c.getName()));
            for(Map.Entry<Double, Double> elem : c.getLocation().entrySet()) {
                hbase.addColumn(Bytes.toBytes("bcabca"), Bytes.toBytes("Location"), Bytes.toBytes(elem.getKey()));
                hbase.addColumn(Bytes.toBytes("bcabca"), Bytes.toBytes("Location"), Bytes.toBytes(elem.getValue()));
            }
            hbase.addColumn(Bytes.toBytes("bcabca"), Bytes.toBytes("Population"), Bytes.toBytes(c.getName()));
            hbase.addColumn(Bytes.toBytes("bcabca"), Bytes.toBytes("State"), Bytes.toBytes(c.getName()));
            hTable.put(hbase);
        }
        Get g = new Get(Bytes.toBytes(99950));
        Result r = hTable.get(g);
        byte[] loc = r.getValue(Bytes.toBytes("bcabca"), Bytes.toBytes("Location"));
        System.out.println(Bytes.toDouble(loc));
    }
}
