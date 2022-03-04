package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpHandlerTest {

    @InjectMocks
    private HttpHandler httpHandler;
    @Mock
    private URL url;
    @Mock
    private HttpURLConnection conn;
    private InputStream inputStream;

    @Before
    public void setUp() throws IOException {
        httpHandler = new HttpHandler(executor);
        url = mock(URL.class);
        conn = mock(HttpURLConnection.class);
        inputStream = new ByteArrayInputStream("anyString".getBytes());
        httpHandler.setUrl(url);
        when(url.openConnection()).thenReturn(conn);
        when(conn.getInputStream()).thenReturn(inputStream);
    }

    @Test
    public void makeServiceCall_ReturnsNullWhenURLisNull() throws NullPointerException {
        httpHandler.setUrl(null);
        String result = httpHandler.makeServiceCall();
        assertNull(result);
    }

    @Test
    public void makeServiceCall_ReturnsString() {
        String result = httpHandler.makeServiceCall();
        assertEquals("anyString\n", result);
    }

    @Test
    public void makeServiceCall_ReturnsString_Correctly() throws IOException {
        when(conn.getInputStream()).thenReturn(new ByteArrayInputStream("anotherString".getBytes()));
        String result = httpHandler.makeServiceCall();
        assertEquals("anotherString\n", result);
    }

}