package com.sunrise.inventoryCheck;

import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;

public class CustomArraySort implements Comparator<LotItem> {

    private String keyToSort;
    private final String DEFAULT_KEY = "warehouse";
    private final List<String> warehouseList = asList("1", "2", "3", "4", "5", "6", "7", "8",
            "9", "11", "12", "12B", "12C", "14", "14A", "15", "16");
    private boolean isDescOrder;

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
    public int compare(LotItem lotItem1, LotItem lotItem2) {
        switch (keyToSort) {
            case "warehouse":
                return sortByWarehouse(lotItem1, lotItem2);
            case "weight":
            case "packs":
                return sortByInteger(lotItem1, lotItem2);
            default:
                return sortByString(lotItem1, lotItem2);
        }
    }

    private int sortByWarehouse(LotItem lotItem1, LotItem lotItem2) {
        int o1Index = warehouseList.contains(lotItem1.getWarehouse()) ?
                warehouseList.indexOf(lotItem1.getWarehouse()) : warehouseList.size() + 1;
        int o2Index = warehouseList.contains(lotItem2.getWarehouse()) ?
                warehouseList.indexOf(lotItem2.getWarehouse()) : warehouseList.size() + 1;
        if (o1Index == o2Index) {
            return 0;
        }
        int result = o1Index < o2Index ? -1 : 1;
        return isDescOrder ? result * (-1) : result;
    }

    private int sortByInteger(LotItem lotItem1, LotItem lotItem2) {
        int value1 = findIntValueBasedOnSortKey(lotItem1);
        int value2 = findIntValueBasedOnSortKey(lotItem2);
        if (value1 == value2) {
            return 0;
        }
        int result = value1 > value2 ? 1 : -1;
        return isDescOrder ? result * (-1) : result;
    }

    private int findIntValueBasedOnSortKey(LotItem lotItem) {
        int result = 0;
        switch (keyToSort) {
            case "weight":
                result = lotItem.getWeight();
                break;
            case "packs":
                result = lotItem.getPacks();
                break;
        }
        return result;
    }

    private int sortByString(LotItem lotItem1, LotItem lotItem2) {
        String value1 = findStringValueBasedOnSortKey(lotItem1);
        String value2 = findStringValueBasedOnSortKey(lotItem2);
        int result = value1.compareTo(value2);
        return isDescOrder ? result * (-1) : result;
    }

    private String findStringValueBasedOnSortKey(LotItem lotitem) {
        String result = null;
        switch (keyToSort) {
            case "polymer":
                result = lotitem.getPolymer();
                break;
            case "packing":
                result = lotitem.getPacking();
                break;
            case "form":
                result = lotitem.getForm();
                break;
            case "compartment":
                result = lotitem.getCompartment();
                break;
            case "grade":
                result = lotitem.getGrade();
                break;
        }
        return result;
        //todo throw when result is null, make sure to write tests
    }
}
