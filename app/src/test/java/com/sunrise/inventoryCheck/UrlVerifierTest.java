package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlVerifierTest {

    private UrlVerifier urlVerifier;

    @Before
    public void setUp() {
        urlVerifier = new UrlVerifier();
    }

    @Test
    public void isValidUrl_ReturnsFalseWhenUrlIsNull() {
        boolean result = urlVerifier.isValidUrlString(null);
        assertFalse(result);
    }

    @Test
    public void isValidUrl_ReturnFalseWhenUrlIsEmpty() {
        boolean result = urlVerifier.isValidUrlString("");
        assertFalse(result);
    }

    @Test
    public void isValidUrl_ReturnsTrueWhenUrlIsValidHttp() {
        boolean result = urlVerifier.isValidUrlString("http://adawd");
        assertTrue(result);
    }

    @Test
    public void isValidUrl_ReturnsTrueWhenUrlIsValidHttps() {
        boolean result = urlVerifier.isValidUrlString("https://adawwawad");
        assertTrue(result);
    }
}