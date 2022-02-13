package com.sunrise.inventoryCheck;

public enum ErrorMessage {

    NullUrl("The API URL is null"),
    InvalidUrl("The API URL is invalid");

    private String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
