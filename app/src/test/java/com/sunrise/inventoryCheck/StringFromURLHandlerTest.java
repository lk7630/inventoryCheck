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

    private static final String DEFAULT_URL_STRING = "http://anyURL";
    private static final String DEFAULT_BACKUP_URL_STRING = "http://anyBackUpURL";
    public static final String JSON_STRING = "anyString";
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
        stringFromURLHandler.setBackUpURLString(DEFAULT_BACKUP_URL_STRING);
        url = new URL(DEFAULT_URL_STRING);
        when(httpHandler.makeServiceCall()).thenReturn(JSON_STRING);
        when(urlVerifier.isValidUrlString(anyString())).thenReturn(true);
    }

    @Test
    public void getStringFromURL_ReturnsStringWhenNoParam() {
        String result = stringFromURLHandler.getStringFromURL();
        assertEquals(JSON_STRING, result);
    }

    @Test
    public void getStringFromURL_ReturnsStringWhenNoParam_Correctly() {
        when(httpHandler.makeServiceCall()).thenReturn("anotherString");
        String result = stringFromURLHandler.getStringFromURL();
        assertEquals("anotherString", result);
    }

    @Test
    public void getStringFromURL_CallsWithUrlOnlyWhenParamIsNull() throws MalformedURLException {
        stringFromURLHandler.getStringFromURL(null);
        verify(httpHandler).setUrl(new URL(DEFAULT_URL_STRING));
    }

    @Test
    public void getStringFromURL_ThrowExceptionWhenParamIsNonNumeric() {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The string is non-numeric. Please make sure all are numbers");
        stringFromURLHandler.getStringFromURL("123as23acs");
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_MakeServiceCall() {
        stringFromURLHandler.getStringFromURL(null);
        verify(httpHandler).makeServiceCall();
    }

    @Test
    public void getStringFromURL_ReturnsWhatHttpHandlerReturns() {
        String result = stringFromURLHandler.getStringFromURL();
        assertEquals(JSON_STRING, result);
    }

    @Test
    public void getStringFromURL_ReturnsWhatHttpHandlerReturns_Correctly() {
        when(httpHandler.makeServiceCall()).thenReturn("anotherString");
        String result = stringFromURLHandler.getStringFromURL();
        assertEquals("anotherString", result);
    }

    @Test
    public void getStringFromURL_CallsHttpHandler() {
        stringFromURLHandler.getStringFromURL();
        verify(httpHandler).setUrl(url);
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_Correctly() throws MalformedURLException {
        stringFromURLHandler.setURLString(DEFAULT_URL_STRING);
        stringFromURLHandler.getStringFromURL("12345");
        verify(httpHandler).setUrl(new URL(DEFAULT_URL_STRING + "12345"));
    }

    @Test
    public void getStringFromURL_ReturnsStringWhenParamIsNull() {
        String result = stringFromURLHandler.getStringFromURL(null);
        assertEquals(JSON_STRING, result);
    }

    @Test
    public void getStringFromURL_ReturnsString_Correctly() {
        when(httpHandler.makeServiceCall()).thenReturn("anotherString");
        String result = stringFromURLHandler.getStringFromURL(null);
        assertEquals("anotherString", result);
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsNull() {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The API URL is null");
        stringFromURLHandler.setURLString(null);
        stringFromURLHandler.getStringFromURL(null);
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsInvalid() {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The API URL is invalid");
        stringFromURLHandler.setURLString("invalidInvalidInvalid");
        stringFromURLHandler.getStringFromURL("12445");
    }

    @Test
    public void getStringFromURL_UsesBackupURLWhenURLReturnsNullString() throws MalformedURLException {
        when(httpHandler.makeServiceCall()).thenReturn(null);
        stringFromURLHandler.getStringFromURL();
        verify(httpHandler).setUrl(new URL(DEFAULT_BACKUP_URL_STRING));
    }
}