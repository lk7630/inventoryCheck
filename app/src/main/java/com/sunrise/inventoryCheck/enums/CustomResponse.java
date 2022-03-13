package com.sunrise.inventoryCheck.enums;

public enum CustomResponse {
    ConnectionOK("Successful connection"),
    ConnectionTimeout("Could not connect"),
    ReadFailure("Failed to download"),
    ReadSuccess("Successfully downloaded"),
    Empty_Content("Lot inventory is empty");

    private String message;

    CustomResponse(String message) {
        this.message = message;
    }

    public String getResponseMessage(){
        return message;
    }
}
