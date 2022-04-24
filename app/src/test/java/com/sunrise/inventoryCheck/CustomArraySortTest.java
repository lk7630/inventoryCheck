package com.sunrise.inventoryCheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static java.util.Arrays.asList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

public class CustomArraySortTest {

    private LotItem lotItem1;
    private LotItem lotItem2;
    private final String WRONG_KEY = "wrongKey";
    private final String DEFAULT_KEY = "warehouse";

    @Mock
    private CustomArraySort customArraySort;

    @Before
    public void setUp() {
        lotItem1 = buildLotItem("HDPE", "PEL", "P", "A", 12, "super sack", 5600, "3");
        lotItem2 = buildLotItem("PPPP", "POW", null, "CA", 14, "box", 8000, "12C");
        customArraySort = new CustomArraySort(WRONG_KEY, false);
    }

    @Test
    public void compare_ComparesString_Polymer() {
        customArraySort.setKeyToSort("polymer");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_ComparesString_Polymer_Desc() {
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("polymer");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result > 0);
    }

    @Test
    public void compare_ComparesString_Polymer_equal() {
        customArraySort.setKeyToSort("polymer");
        lotItem2.setPolymer("HDPE");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_weight() {
        customArraySort.setKeyToSort("weight");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_weight_Desc() {
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("weight");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result > 0);
    }

    @Test
    public void compare_CompareString_weight_equal() {
        customArraySort.setKeyToSort("weight");
        lotItem2.setWeight(5600);
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_warehouse() {
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_warehouse_Desc() {
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result < 0);
    }

    @Test
    public void compare_CompareString_warehouse_equal() {
        lotItem1.setWarehouse("12C");
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertEquals(0, result);
    }

    @Test
    public void compare_CompareString_warehouse_ValueNotInList() {
        lotItem1.setWarehouse("50AB");
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result > 0);
    }

    @Test
    public void compare_CompareString_warehouse_ValueNotInList_Desc() {
        lotItem1.setWarehouse("50AB");
        customArraySort.setDescOrder(true);
        customArraySort.setKeyToSort("warehouse");
        int result = customArraySort.compare(lotItem1, lotItem2);
        assertTrue(result < 0);
    }

    private LotItem buildLotItem(String polymer, String form, String grade, String compartment,
                                 int packs, String packing, int weight, String warehouse) {
        LotItem lotItem = new LotItem();
        lotItem.setPolymer(polymer);
        lotItem.setForm(form);
        lotItem.setCompartment(compartment);
        lotItem.setGrade(grade);
        lotItem.setPacks(packs);
        lotItem.setPacking(packing);
        lotItem.setWeight(weight);
        lotItem.setWarehouse(warehouse);
        return lotItem;
    }
}

