package com.example.dhruv.cbrmp3index.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dhruv on 31/3/17.
 */

public class RecvdDataModel {

    public static String DATA_TAG = "data";
    public static String ID_TAG = "tag";
    public static String NAME_TAG = "name";
    public static String TYPE_TAG = "type";

    private String id;
    private String name;
    private String type;
    private JSONArray data;

    public void mapAll(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getString(ID_TAG));
        setData(jsonObject.getJSONArray(DATA_TAG));
        setName(jsonObject.getString(NAME_TAG));
        setType(jsonObject.getString(TYPE_TAG));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
