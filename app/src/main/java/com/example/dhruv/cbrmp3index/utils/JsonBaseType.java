package com.example.dhruv.cbrmp3index.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dhruv on 31/3/17.
 */

public class JsonBaseType {

    static String VERSION_TAG = "version";
    static String DATA_TAG = "data";

    public static int getVersion(JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(VERSION_TAG);
    }

    public static JSONArray getJsonArray(JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONArray(DATA_TAG);
    }
}
