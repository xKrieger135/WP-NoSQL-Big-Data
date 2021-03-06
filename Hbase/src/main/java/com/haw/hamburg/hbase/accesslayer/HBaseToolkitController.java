package com.haw.hamburg.hbase.accesslayer;

import com.haw.hamburg.util.filereaderadapter.FileReaderAdapter;
import com.haw.hamburg.util.filereaderadapter.interfaces.IFileReaderAdapter;
import com.haw.hamburg.util.jsonconverteradapter.JSONConverterAdapter;
import com.haw.hamburg.util.jsonconverteradapter.interfaces.IJSONConverterAdapter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONString;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 5/4/16.
 *
 * The reason for GET HTTP methods is, that time goes on and there isn't enough time to change it to the correct way.
 *
 */
@RestController
public class HBaseToolkitController {

    @Autowired
    private HBaseToolkitFassade hBaseToolkitFassade;

    public HBaseToolkitController() {

    }

    @RequestMapping(value = "/import/{tablename}/{columnfamilyname}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.CREATED)
    public void databaseImport(@PathVariable String tablename, @PathVariable String columnfamilyname) throws IOException, JSONException {
        IFileReaderAdapter fra = new FileReaderAdapter();
        IJSONConverterAdapter jca = new JSONConverterAdapter();
        List<String> importData = fra.readFile("/home/nosql/Documents/WP-NoSQL-Big-Data/Doc/plz.data");
        List<JSONObject> jsonObjects = jca.convertToJSONList(importData);
        try {
            hBaseToolkitFassade.databaseImport(jsonObjects, tablename, columnfamilyname);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/search/city/{postalcode}", method = RequestMethod.GET, produces = "application/json")
    public String searchCityByPostalcode(@PathVariable String postalcode) throws IOException, JSONException {
        JSONObject j = hBaseToolkitFassade.searchCityByPostalcode("BigData", "CityData", postalcode);
        return j.toString();
    }

    @RequestMapping(value = "/search/postalcode/{city}", method = RequestMethod.GET, produces = "application/json")
    public String searchPostalcodeByCity(@PathVariable String city) throws IOException, JSONException {
        JSONObject j = hBaseToolkitFassade.searchPostalcodeByCity("BigData", "CityData", city);

        return j.toString();
    }

    @RequestMapping(value = "/create/{table}/{columnFamily}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> createTable(@PathVariable String table, @PathVariable String columnFamily) throws IOException {
        hBaseToolkitFassade.createTable(table, columnFamily);
        return new ResponseEntity<>(HttpStatus.CREATED.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/add/{table}/{columnFamily}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<String> addColumnFamily(@PathVariable String table, @PathVariable String columnFamily) throws IOException {
        hBaseToolkitFassade.addColumnFamily(table, columnFamily);
        return new ResponseEntity<>(HttpStatus.CREATED.toString(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/drop/{table}")
    public ResponseEntity<String> dropTable(@PathVariable String table) throws IOException {
        hBaseToolkitFassade.dropTable(table);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/add/{table}/{columnFamily}/{value}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> addColumnFamilyValue(@PathVariable String table, @PathVariable String columnFamily, @PathVariable String value) throws IOException {
        String rowIdentifier = "";
        hBaseToolkitFassade.addColumnFamilyValue(table, columnFamily, rowIdentifier, value);
        return new ResponseEntity<>(HttpStatus.OK.toString(), HttpStatus.OK);
    }
}
