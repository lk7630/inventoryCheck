package com.example.jsontest;

import java.util.Comparator;
import java.util.HashMap;

public class CustomArraySort implements Comparator<HashMap<Object, Object>> {

    private String keyToSort;

    public CustomArraySort(String keyToSort) {
        this.keyToSort = keyToSort;
    }

    @Override
    public int compare(HashMap<Object, Object> o1, HashMap<Object, Object> o2) {
        if (keyToSort == "warehouse" || keyToSort == "weight" || keyToSort == "packs") {
            if (o1.get(keyToSort) == o2.get(keyToSort)) {
                return 0;
            }
            return (int) o1.get(keyToSort) > (int) o2.get(keyToSort) ? 1 : -1;
        }
        return String.valueOf(o1.get(keyToSort)).compareTo(String.valueOf(o2.get(keyToSort)));
    }
}
