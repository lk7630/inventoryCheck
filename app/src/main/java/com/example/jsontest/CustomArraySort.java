package com.example.jsontest;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

public class CustomArraySort implements Comparator<HashMap<Object, Object>> {

    private String keyToSort;
    private final String DEFAULT_KEY = "warehouse";
    private List<String> warehouseList = asList("1", "2", "3", "4", "5", "6", "7", "8",
            "9", "11", "12", "12B", "12C", "14", "14A", "15", "16");
    private boolean isDescOrder = false;

    public String getKeyToSort() {
        return keyToSort;
    }

    public void setKeyToSort(String keyToSort) {
        this.keyToSort = keyToSort;
    }

    public CustomArraySort(String keyToSort, boolean isDescOrder) {
        this.keyToSort = keyToSort;
        this.isDescOrder = isDescOrder;
    }

    public void setDescOrder(boolean descOrder) {
        isDescOrder = descOrder;
    }

    @Override
    public int compare(HashMap<Object, Object> o1, HashMap<Object, Object> o2) {
        if (!o1.containsKey(keyToSort)) {
            keyToSort = DEFAULT_KEY;
        }
        if (keyToSort.equals("warehouse")) {
            return sortByWarehouse(o1, o2);
        }
        if (keyToSort.equals("weight") || keyToSort.equals("packs")) {
            if (o1.get(keyToSort) == o2.get(keyToSort)) {
                return 0;
            }
            int result = Integer.parseInt((String) o1.get(keyToSort))
                    > Integer.parseInt((String) o2.get(keyToSort))
                    ? 1 : -1;
            return isDescOrder ? result * (-1) : result;
        }
        int result = String.valueOf(o1.get(keyToSort)).compareTo(String.valueOf(o2.get(keyToSort)));
        return isDescOrder ? result * (-1) : result;
    }

    private int sortByWarehouse(HashMap<Object, Object> o1, HashMap<Object, Object> o2) {
        int o1Index = warehouseList.contains((String) o1.get("warehouse")) ?
                warehouseList.indexOf((String) o1.get("warehouse")) : warehouseList.size() + 1;
        int o2Index = warehouseList.contains((String) o2.get("warehouse")) ?
                warehouseList.indexOf((String) o2.get("warehouse")) : warehouseList.size() + 1;
        if (o1Index == o2Index) {
            return 0;
        }
        int result = o1Index < o2Index ? -1 : 1;
        return isDescOrder ? result * (-1) : result;
    }
}
