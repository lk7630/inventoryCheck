package com.sunrise.inventoryCheck;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JsonHandlerTest {

    @Mock
    JSONObject jsonObject;
    @InjectMocks
    private JsonHandler jsonHandler;
    private String jsonList;
    public static final List<String> LOT_ITEM_KEYS = new ArrayList<>(asList("polymer", "form", "packs", "packing", "weight",
            "warehouse", "compartment", "grade"));

    @Before
    public void setUp(){
        jsonHandler = new JsonHandler();
        jsonObject = buildTestJSONObject();
        jsonList = jsonObject.toString();
    }

    @Test
    public void getHashMapFromJson_ReturnsEmptyHashmapWhenjsonStrIsNull() {
        jsonList = null;
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void getHashMapFromJson_ReturnsHashMapWhenLotItemListIsEmpty() throws JSONException {
        jsonObject.put("lotItems", new ArrayList<>());
        jsonList = jsonObject.toString();
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertEquals(new ArrayList<>(), result.get("lotItems"));
    }

    @Test
    public void getHashMapFromJson_ReturnsNullHasHMapWhenLotItemListIsNullString() throws JSONException {
        jsonObject.put("lotItems", "null");
        jsonList = jsonObject.toString();
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertNull(result);
    }

    @Test
    public void getHashMapFromJson_ReturnsNullHasHMapWhenLotItemListIsNull() throws JSONException {
        jsonObject.put("lotItems", null);
        jsonList = jsonObject.toString();
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertNull(result);
    }

    @Test
    public void getHashMapFromJson_ReturnsHashMapWhenAValueIsEmpty() throws JSONException {
        jsonObject.put("folder", "");
        jsonList = jsonObject.toString();
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertEquals("", result.get("folder"));
    }

    @Test
    public void getHashMapFromJson_ReturnsNullHashMapWhenAValueIsNullString() throws JSONException {
        jsonObject.put("lot", "null");
        jsonList = jsonObject.toString();
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertEquals("", result.get("lot"));
    }

    @Test
    public void getHashMapFromJson_ReturnsHashMapWhenAValueInLotItemsIsEmpty() throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("lotItems");
        jsonArray.put(buildJSONObject(LOT_ITEM_KEYS,
                asList("PS", "PEL", "200", "box", "10000", "9", "", "")));
        jsonObject.put("lotItems", jsonArray);
        jsonList = jsonObject.toString();
        List<HashMap<Object, Object>> resultList =
                (List<HashMap<Object, Object>>)
                        jsonHandler.getHashMapFromJson(jsonList).get("lotItems");
        assertNotNull(resultList);
        HashMap<Object, Object> resultHashmap = resultList.get(resultList.size() - 1);
        assertEquals("", resultHashmap.get("compartment"));
        assertEquals("", resultHashmap.get("grade"));
    }

    @Test
    public void getHashMapFromJson_ReturnsHashMapWhenAValueIsAStringNullString() throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("lotItems");
        jsonArray.put(buildJSONObject(LOT_ITEM_KEYS,
                asList("PS", "PEL", "200", "box", "10000", "9", "null", "null")));
        jsonObject.put("lotItems", jsonArray);
        jsonList = jsonObject.toString();
        List<HashMap<Object, Object>> resultList =
                (List<HashMap<Object, Object>>)
                        jsonHandler.getHashMapFromJson(jsonList).get("lotItems");
        assertNotNull(resultList);
        HashMap<Object, Object> resultHashmap = resultList.get(resultList.size() - 1);
        assertEquals("", resultHashmap.get("compartment"));
        assertEquals("", resultHashmap.get("grade"));
    }

    private JSONObject buildTestJSONObject() {
        JSONObject returnJSONObject = buildJSONObject(asList("panID", "folder", "lot"),
                asList("179310", "PLANT", "2210713"));
        JSONObject lotItemObject1 = buildJSONObject(LOT_ITEM_KEYS,
                asList("HDPE", "PEL", "1", "bulk", "800", "3", "A", "PARTIAL"));
        JSONObject lotItemObject2 = buildJSONObject(LOT_ITEM_KEYS,
                asList("PP", "RG", "10", "super sack", "22000", "12A", "B", "null"));
        JSONObject lotItemObject3 = buildJSONObject(LOT_ITEM_KEYS,
                asList("LDPE", "POW", "20", "box", "30000", "3", "CA", "PARTIAL"));
        JSONObject lotItemObject4 = buildJSONObject(LOT_ITEM_KEYS,
                asList("LLDPE", "PEL", "100", "bag", "5500", "16", "CB", "null"));
        JSONArray jsonArray = buildJSONArray(asList(lotItemObject1, lotItemObject2,
                lotItemObject3, lotItemObject4));
        try {
            returnJSONObject.put("lotItems", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnJSONObject;
    }

    private JSONObject buildJSONObject(List<String> keys, List<String> values) {
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < keys.size(); i++) {
                jsonObject.put(keys.get(i), values.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray buildJSONArray(List<JSONObject> jsonObjects) {
        JSONArray jsonArray = new JSONArray();
        jsonObjects.forEach(jsonArray::put);
        return jsonArray;
    }
}