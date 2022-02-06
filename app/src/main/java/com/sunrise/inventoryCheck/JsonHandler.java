package com.sunrise.inventoryCheck;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonHandler {

    private List<HashMap<Object, Object>> lotItemList;

    public List<HashMap<Object, Object>> getLotItemList() {
        return lotItemList;
    }

    public JsonHandler() {
    }

    public HashMap<Object, Object> getHashMapFromJson(String jsonStr) {
        HashMap<Object, Object> jsonHashMap = new HashMap<>();
        if (jsonStr != null) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("lotItems");
                jsonHashMap.put("panID", parseStringFromJsonObject(jsonObject, "panID"));
                jsonHashMap.put("folder", parseStringFromJsonObject(jsonObject, "folder"));
                jsonHashMap.put("lot", parseStringFromJsonObject(jsonObject, "lot"));

                lotItemList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<Object, Object> lotItems = new HashMap<>();
                    JSONObject lotItemsObject = jsonArray.getJSONObject(i);
                    lotItems.put("polymer", parseStringFromJsonObject(lotItemsObject, "polymer"));
                    lotItems.put("form", parseStringFromJsonObject(lotItemsObject, "form"));
                    lotItems.put("compartment", parseStringFromJsonObject(lotItemsObject, "compartment"));
                    lotItems.put("grade", parseStringFromJsonObject(lotItemsObject, "grade"));
                    lotItems.put("packs", parseStringFromJsonObject(lotItemsObject, "packs"));
                    lotItems.put("packing", parseStringFromJsonObject(lotItemsObject, "packing"));
                    lotItems.put("weight", parseStringFromJsonObject(lotItemsObject, "weight"));
                    lotItems.put("warehouse", parseStringFromJsonObject(lotItemsObject, "warehouse"));
                    lotItemList.add(lotItems);
                }
                jsonHashMap.put("lotItems", lotItemList);
            } catch (JSONException e) {
                Log.e("error", "Json parsing error: " + e.getMessage());
                return null;
            }
        }
        return jsonHashMap;
    }

    private String parseStringFromJsonObject(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key).equals("null") || jsonObject.getString(key).equals("")
                    ? "" : jsonObject.getString(key);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error", "Cannot get value from key " + key);
            return null;
        }
    }
}