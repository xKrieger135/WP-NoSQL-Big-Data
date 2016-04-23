package com.haw.hamburg.util.jsonconverteradapter.interfaces;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.List;

/**
 * Created by nosql on 4/23/16.
 */
public interface IJSONConverterAdapter {

    /**
     * This method converts a given list with Strings into a new list with JSONObjects.
     *
     * @param fileInput The fileInput is the data from the given file as an ArrayList with Strings. Each String
     *                  represents a line from the file.
     * @return A list with data from given file as JSONObjects.
     * @throws JSONException
     */
    List<JSONObject> convertToJSONList(List<String> fileInput) throws JSONException;

}
