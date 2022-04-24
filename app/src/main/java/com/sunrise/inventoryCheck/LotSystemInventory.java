package com.sunrise.inventoryCheck;

import java.util.ArrayList;
import java.util.List;

public class LotSystemInventory {
    private String panID;
    private String folder;
    private String lot;
    private List<LotItem> lotItems = new ArrayList<>();

    public String getPanID() {
        return panID;
    }

    public void setPanID(String panID) {
        this.panID = panID;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public List<LotItem> getLotItems() {
        return lotItems;
    }

    public void setLotItems(List<LotItem> lotItems) {
        this.lotItems = lotItems;
    }
}
