package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StringFromURLHandlerTest {

    public static final String DEFAULT_URL = "http://192.168.168.8:10081/plastic/GetLotInfo/";


    @InjectMocks
    StringFromURLHandler stringFromURLHandler;
    @Mock
    HttpHandler httpHandler;

    @Before
    public void setUp() {
        httpHandler = mock(HttpHandler.class);
        stringFromURLHandler = new StringFromURLHandler(httpHandler);
        stringFromURLHandler.setURLString(DEFAULT_URL);
        when(httpHandler.makeServiceCall()).thenReturn("anyString");
    }

    @Test
    public void getStringFromURL_ParamIsNull() {
//        stringFromURLHandler.getStringFromURL(null);
//        URL url = httpHandler.getUrl();
//        assertEquals(DEFAULT_URL,url.toString());
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
    public void getStringFromURL_CallsHttpHandler(){
        stringFromURLHandler.getStringFromURL("anyParam");
        verify(httpHandler).setUrlFromString(DEFAULT_URL + "anyParam");
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_Correctly(){
        stringFromURLHandler.setURLString("anotherString");
        stringFromURLHandler.getStringFromURL("anyParam");
        verify(httpHandler).setUrlFromString("anotherString" + "anyParam");
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
    public void getStringFromURL_WhenURLIsNull(){

    }

    @Test
    public void getStringFromURL_WhenURLIsInvalid(){

    }
}