package com.example.jsontest;

import java.util.Comparator;
import java.util.HashMap;

public class CustomArraySort implements Comparator<HashMap<Object, Object>> {

    private String keyToSort;
    private final String DEFAULT_KEY = "warehouse";

    public String getKeyToSort() {
        return keyToSort;
    }

    public void setKeyToSort(String keyToSort) {
        this.keyToSort = keyToSort;
    }

    public CustomArraySort(String keyToSort) {
        this.keyToSort = keyToSort;
    }

    @Override
    public int compare(HashMap<Object, Object> o1, HashMap<Object, Object> o2) {
        if (keyToSort.equals("weight") || keyToSort.equals("packs")) {
            if (o1.get(keyToSort) == o2.get(keyToSort)) {
                return 0;
            }
            return Integer.parseInt((String)o1.get(keyToSort))
                    > Integer.parseInt((String)o2.get(keyToSort))
                    ? 1 : -1;
        }
        return String.valueOf(o1.get(keyToSort)).compareTo(String.valueOf(o2.get(keyToSort)));
    }
}
