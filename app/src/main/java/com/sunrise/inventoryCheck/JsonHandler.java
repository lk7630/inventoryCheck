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
                jsonHashMap.put("panID", jsonObject.getInt("panID"));
                jsonHashMap.put("folder", jsonObject.getString("folder"));
                jsonHashMap.put("lot", jsonObject.getInt("lot"));
                lotItemList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    HashMap<Object, Object> lotItems = new HashMap<>();
                    JSONObject lotItemsObject = jsonArray.getJSONObject(i);
                    lotItems.put("polymer", lotItemsObject.getString("polymer"));
                    lotItems.put("form", lotItemsObject.getString("form"));
                    lotItems.put("compartment", lotItemsObject.getString("compartment"));
                    lotItems.put("grade", lotItemsObject.getString("grade"));
                    lotItems.put("packs", lotItemsObject.getString("packs"));
                    lotItems.put("packing", lotItemsObject.getString("packing"));
                    lotItems.put("weight", lotItemsObject.getString("weight"));
                    lotItems.put("warehouse", lotItemsObject.getString("warehouse"));
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
}