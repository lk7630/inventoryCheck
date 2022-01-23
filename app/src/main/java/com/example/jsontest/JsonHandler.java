package com.example.jsontest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonHandler {

    public JsonHandler() {
    }

    public HashMap<Object, Object> getHashMapFromJson(String jsonStr) {
        HashMap<Object, Object> jsonHashMap = new HashMap<>();
        HashMap<Object, Object> lotItems = new HashMap<>();
        if (jsonStr != null) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("lotItems");
                jsonHashMap.put("panID", jsonObject.getInt("panID"));
                jsonHashMap.put("folder", jsonObject.getString("folder"));
                jsonHashMap.put("lot", jsonObject.getInt("lot"));
                List<HashMap<Object, Object>> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject lotItemsObject = jsonArray.getJSONObject(i);
                    lotItems.put("polymer", lotItemsObject.getString("polymer"));
                    lotItems.put("form", lotItemsObject.getString("form"));
                    lotItems.put("packs", lotItemsObject.getInt("packs"));
                    lotItems.put("packing", lotItemsObject.getString("packing"));
                    lotItems.put("weight", lotItemsObject.getInt("weight"));
                    lotItems.put("warehouse", lotItemsObject.getInt("warehouse"));
                    list.add(lotItems);
                }
                jsonHashMap.put("lotItems", list);

            } catch (JSONException e) {
                Log.e("error", "Json parsing error: " + e.getMessage());
                return null;
            }
        }
        return jsonHashMap;
    }
}