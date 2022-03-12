package com.sunrise.inventoryCheck.enums;

public enum CustomResponse {
    ConnectionOK("Successful connection"),
    ConnectionTimeout("Could not connect"),
    ReadFailure("Failed to download"),
    EmptyContent("Lot inventory is empty");

    private String message;

    CustomResponse(String message) {
        this.message = message;
    }

    public String getResponseMessage(){
        return message;
    }
}
