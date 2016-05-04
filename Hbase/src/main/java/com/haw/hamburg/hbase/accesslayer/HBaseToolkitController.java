package com.haw.hamburg.hbase.accesslayer;

import com.haw.hamburg.util.filereaderadapter.FileReaderAdapter;
import com.haw.hamburg.util.filereaderadapter.interfaces.IFileReaderAdapter;
import com.haw.hamburg.util.jsonconverteradapter.JSONConverterAdapter;
import com.haw.hamburg.util.jsonconverteradapter.interfaces.IJSONConverterAdapter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by nosql on 5/4/16.
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

    @RequestMapping(value = "/search/{postalcode}", method = RequestMethod.GET)
    public String searchCityByPostalcode(@PathVariable String postalcode) throws IOException, JSONException {
        JSONObject j = hBaseToolkitFassade.searchCityByPostalcode("BigData", "CityData", postalcode);
        return j.toString();
    }

    @RequestMapping(value = "/search/{city}", method = RequestMethod.GET)
    public String searchPostalcodeByCity(@PathVariable String city) throws IOException {
        JSONObject j = hBaseToolkitFassade.searchPostalcodeByCity("BigData", "CityData", city);
        return j.toString();
    }
}
