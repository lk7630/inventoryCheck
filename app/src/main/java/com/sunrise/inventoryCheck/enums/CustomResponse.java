package com.sunrise.inventoryCheck.enums;

public enum CustomResponse {
    ConnectionOK("Successful connection"),
    ConnectionTimeout("Could not connect"),
    ReadFailure("Failed to download"),
    ReadSuccess("Successfully downloaded"),
    EmptyContent("Lot inventory is empty"),
    NotConnected("No internet connection");

    private String message;

    CustomResponse(String message) {
        this.message = message;
    }

    public String getResponseMessage(){
        return message;
    }
}
