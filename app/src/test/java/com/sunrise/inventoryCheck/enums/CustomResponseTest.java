package com.sunrise.inventoryCheck.enums;

import static com.sunrise.inventoryCheck.enums.CustomResponse.ConnectionOK;
import static com.sunrise.inventoryCheck.enums.CustomResponse.ConnectionTimeout;
import static com.sunrise.inventoryCheck.enums.CustomResponse.EmptyContent;
import static com.sunrise.inventoryCheck.enums.CustomResponse.NotConnected;
import static com.sunrise.inventoryCheck.enums.CustomResponse.ReadFailure;
import static com.sunrise.inventoryCheck.enums.CustomResponse.ReadSuccess;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CustomResponseTest {

    @Test
    public void getResponseMessage_ConnectionOK(){
        assertEquals("Successful connection", ConnectionOK.getResponseMessage());
    }

    @Test
    public void getResponseMessage_ConnectionTimeout(){
        assertEquals("Could not connect", ConnectionTimeout.getResponseMessage());
    }

    @Test
    public void getResponseMessage_ReadFailure(){
        assertEquals("Failed to download", ReadFailure.getResponseMessage());
    }

    @Test
    public void getResponseMessage_ReadSuccess(){
        assertEquals("Successfully downloaded", ReadSuccess.getResponseMessage());
    }

    @Test
    public void getResponseMessage_EmptyContent(){
        assertEquals("Lot inventory is empty", EmptyContent.getResponseMessage());
    }

    @Test
    public void getResponseMessage_NotConnected(){
        assertEquals("No internet connection", NotConnected.getResponseMessage());
    }
}