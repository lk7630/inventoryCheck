package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StringFromURLHandlerTest {

    public static final String DEFAULT_URL = "http://192.168.168.8:10081/plastic/GetLotInfo/";
    StringFromURLHandler stringFromURLHandler;
    @Mock
    HttpHandler httpHandler;

    @Before
    public void setUp() {
        stringFromURLHandler = new StringFromURLHandler();
        httpHandler = new HttpHandler();
        stringFromURLHandler.setURL(DEFAULT_URL);
 //       when(httpHandler.makeServiceCall(anyString())).thenReturn("anyString");
    }

   @Test
    public void getStringFromURL_ParamIsNull(){
        
   }

    @Test
    public void getStringFromURL_ParamIsNonString(){

    }

    @Test
    public void getStringFromURL_ReturnString(){

    }

    @Test
    public void getStringFromURL_CallsHttpHandler(){
        stringFromURLHandler.getStringFromURL("anyString");
        verify(httpHandler).makeServiceCall();
    }
}