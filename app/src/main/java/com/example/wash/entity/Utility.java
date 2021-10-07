package com.example.wash.entity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class Utility {
    public static DataAll handleWashersResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject jsonArray = jsonObject.getJSONObject("data");
            String content = "";
            content = jsonArray.toString();
            return new Gson().fromJson(content, DataAll.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
