package com.sunrise.inventoryCheck;

import java.time.LocalDate;

public class LastInventory {
    private String wareHouse;
    private LocalDate date;
    private String section;
    private int packs;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getPacks() {
        return packs;
    }

    public void setPacks(int packs) {
        this.packs = packs;
    }
}
