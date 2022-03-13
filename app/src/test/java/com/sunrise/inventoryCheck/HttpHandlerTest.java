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
        httpHandler = new HttpHandler();
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
        httpHandler.makeServiceCall();
        assertNull(httpHandler.getDownloadContent());
    }

    @Test
    public void makeServiceCall_ReturnsString() {
        httpHandler.makeServiceCall();
        assertEquals("anyString\n", httpHandler.getDownloadContent());
    }

    @Test
    public void makeServiceCall_ReturnsString_Correctly() throws IOException {
        when(conn.getInputStream()).thenReturn(new ByteArrayInputStream("anotherString".getBytes()));
        httpHandler.makeServiceCall();
        assertEquals("anotherString\n", httpHandler.getDownloadContent());
    }

}