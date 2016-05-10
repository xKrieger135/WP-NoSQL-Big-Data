package com.haw.hamburg.util.jsonconverteradapter;


import com.haw.hamburg.util.jsonconverteradapter.interfaces.IJSONConverterAdapter;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nosql on 4/23/16.
 */
public class JSONConverterAdapter implements IJSONConverterAdapter {

    public JSONConverterAdapter() {

    }

    @Override
    public List<JSONObject> convertToJSONList(List<String> fileInput) throws JSONException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (String line : fileInput) {
            JSONObject jsonObject = new JSONObject(line);
            jsonObjects.add(jsonObject);
        }
        return jsonObjects;
    }
}
