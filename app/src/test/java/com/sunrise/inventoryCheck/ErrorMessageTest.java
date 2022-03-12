package com.sunrise.inventoryCheck;

import org.junit.Test;

import static com.sunrise.inventoryCheck.enums.ErrorMessage.InvalidUrl;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NonNumericString;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NullUrl;
import static org.junit.Assert.*;

public class ErrorMessageTest {

    @Test
    public void getErrorMessage_NullUrl() {
        assertEquals("The API URL is null", NullUrl.getErrorMessage());
    }

    @Test
    public void getErrorMessage_InvalidUrl() {
        assertEquals("The API URL is invalid", InvalidUrl.getErrorMessage());
    }

    @Test
    public void getErrorMessage_NonNumericString() {
        assertEquals("The string is non-numeric. Please make sure all are numbers",
                NonNumericString.getErrorMessage());
    }
}