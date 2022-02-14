package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StringFromURLHandlerTest {

    private static final String DEFAULT_URL_STRING = "http://192.168.168.8:10081/plastic/GetLotInfo/";
    private URL url;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @InjectMocks
    private StringFromURLHandler stringFromURLHandler;
    @Mock
    private HttpHandler httpHandler;
    @Mock
    private UrlVerifier urlVerifier;

    @Before
    public void setUp() throws MalformedURLException {
        httpHandler = mock(HttpHandler.class);
        urlVerifier = mock(UrlVerifier.class);
        stringFromURLHandler = new StringFromURLHandler(httpHandler);
        stringFromURLHandler.setURLString(DEFAULT_URL_STRING);
        url = new URL(DEFAULT_URL_STRING);
        when(httpHandler.makeServiceCall()).thenReturn("anyString");
        when(urlVerifier.isValidUrlString(anyString())).thenReturn(true);
    }

    @Test
    public void getStringFromURL_CallsWithUrlOnlyWhenParamIsNull() {
        stringFromURLHandler.getStringFromURL(null);
        verify(httpHandler).setUrlFromString(DEFAULT_URL_STRING);

    }

    @Test
    public void getStringFromURL_ParamIsNonString() {

    }

    @Test
    public void getStringFromURL_CallsHttpHandler_MakeServiceCall() {
        stringFromURLHandler.getStringFromURL(null);
        verify(httpHandler).makeServiceCall();
    }

    @Test
    public void getStringFromURL_CallsHttpHandler() throws MalformedURLException {
        stringFromURLHandler.getStringFromURL();
        assertEquals(url,httpHandler.getUrl());
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_Correctly() {
        stringFromURLHandler.setURLString("http://sawaad");
        stringFromURLHandler.getStringFromURL("anyParam");
        verify(httpHandler).setUrlFromString("http://sawaad" + "anyParam");
    }

    @Test
    public void getStringFromURL_ReturnString() {
        String result = stringFromURLHandler.getStringFromURL(null);
        assertEquals("anyString", result);
    }

    @Test
    public void getStringFromURL_ReturnString_Correctly() {
        when(httpHandler.makeServiceCall()).thenReturn("anotherString");
        String result = stringFromURLHandler.getStringFromURL(null);
        assertEquals("anotherString", result);
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsNull() {
        thrown.expect(InvalidURL.class);
        thrown.expectMessage("The API URL is null");
        stringFromURLHandler.setURLString(null);
        stringFromURLHandler.getStringFromURL("any");
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsInvalid() {
        thrown.expect(InvalidURL.class);
        thrown.expectMessage("The API URL is invalid");
        stringFromURLHandler.setURLString("invalidInvalidInvalid");
        stringFromURLHandler.getStringFromURL("any");
    }
}