package com.sunrise.inventoryCheck.enums;

public enum ErrorMessage {

    NullUrl("The API URL is null"),
    InvalidUrl("The API URL is invalid"),
    NonNumericString("The string is non-numeric. Please make sure all are numbers");

    private String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
