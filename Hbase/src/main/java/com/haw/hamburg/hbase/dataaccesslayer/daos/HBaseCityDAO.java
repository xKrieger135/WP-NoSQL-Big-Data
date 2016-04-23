package com.haw.hamburg.hbase.dataaccesslayer.daos;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Created by Krieger135 on 4/22/16.
 */
public class HBaseCityDAO {

    private HBaseAdmin hBaseAdmin = null;
    private Configuration configuration = null;

    public HBaseCityDAO(HBaseAdmin hBaseAdmin, Configuration configuration) {
        this.hBaseAdmin = hBaseAdmin;
        this.configuration = configuration;
    }

    private void dataImport() {

    }
}
