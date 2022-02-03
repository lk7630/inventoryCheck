package com.sunrise.inventoryCheck;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class JsonHandlerTest {

    @Mock
    JSONObject jsonObject;
    @InjectMocks
    private JsonHandler jsonHandler;
    private String jsonList;

    @Before
    public void setUp() throws JSONException {
        jsonHandler = new JsonHandler();
        jsonList = buildJsonString();
        when(jsonHandler.getHashMapFromJson(anyString())).thenCallRealMethod();
        when(jsonObject.getJSONArray(any())).thenReturn(null);
    }

    @Test
    public void getHashMapFromJson_ReturnsEmptyHashmapWhenjsonStrIsNull() {
//        jsonList = null;
        HashMap<Object, Object> result = jsonHandler.getHashMapFromJson(jsonList);
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void getHashMapFromJson_ResultWhenLotItemListIsEmpty() {

    }

    @Test
    public void getHashMapFromJson_ResultWhenLotItemListIsNull() {

    }

    @Test
    public void getHashMapFromJson_ResultWhenAKeyIsEmpty() {

    }

    @Test
    public void getHashMapFromJson_ResultWhenAKeyIsNull() {

    }

    private String buildJsonString() {
        return "{\"panID\":\"180310\",\"folder\":\"RGPP\",\"lot\":\"2210028\",\"lotItems\"" +
                ":[{\"polymer\":\"PP\",\"form\":\"RG\",\"packs\":\"9\",\"packing\":" +
                "\"super sack\",\"weight\":\"10800\",\"warehouse\":\"14\"},{\"polymer" +
                "\":\"HDPE\",\"form\":\"PEL\",\"packs\":\"2\",\"packing\":\"box\"," +
                "\"weight\":\"2400\",\"warehouse\":\"16\"}]}";
    }

}