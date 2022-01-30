package com.example.jsontest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class CustomArraySortTest {

    private HashMap<Object, Object> o1;
    private HashMap<Object, Object> o2;
    private final String WRONG_KEY = "wrongKey";
    private final String DEFAULT_KEY = "warehouse";

    @Mock
    private CustomArraySort customArraySort;

    @Before
    public void setUp() {
        List<String> keys = asList("polymer", "form", "packs", "packing", "weight", "warehouse");
        o1 = setUpHashMapObject(keys, asList("HDPE", "PEL", "12", "super sack", "5600", "3"));
        o2 = setUpHashMapObject(keys, asList("PPPP", "POW", "14", "box", "8000", "12C"));
        customArraySort = new CustomArraySort(WRONG_KEY, false);
    }

    @Test
    public void compare_SwitchToDefaultKey(){
        customArraySort.compare(o1, o2);
        String result=customArraySort.getKeyToSort();
        assertEquals(DEFAULT_KEY, result);
    }

    @Test
    public void compare_SwitchToDefaultKey_Correctly(){
        customArraySort.setKeyToSort("anyKey");
        customArraySort.compare(o1, o2);
        String result = customArraySort.getKeyToSort();
        assertEquals(DEFAULT_KEY, result);
    }

    @Test
    public void compare_ComparesString_Polymer() {
        customArraySort.setKeyToSort("polymer");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result<0);
    }

    @Test
    public void compare_ComparesString_Polymer_Desc() {
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("polymer");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result>0);
    }

    @Test
    public void compare_ComparesString_Polymer_equal() {
        customArraySort.setKeyToSort("polymer");
        o2.replace("polymer", "HDPE");
        int result = customArraySort.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_weight(){
        customArraySort.setKeyToSort("weight");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_weight_Desc(){
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("weight");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void compare_CompareString_weight_equal(){
        customArraySort.setKeyToSort("weight");
        o2.replace("weight", "5600");
        int result = customArraySort.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_warehouse(){
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_warehouse_Desc(){
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_warehouse_equal(){
        o1.replace("warehouse", "12C");
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_warehouse_ValueNotInList(){
        o1.replace("warehouse", "50AB");
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result > 0);
    }

    @Test
    public void compare_CompareString_warehouse_ValueNotInList_Desc(){
        o1.replace("warehouse", "50AB");
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(o1, o2);
        assertTrue(result < 0);
    }

    private HashMap<Object, Object> setUpHashMapObject(List<String> keys, List<String> values) {
        HashMap<Object, Object> returnHashmap = new HashMap<>();
        for (int i=0; i<keys.size();i++){
            returnHashmap.put(keys.get(i), values.get(i));
        }
        return returnHashmap;
    }
}

