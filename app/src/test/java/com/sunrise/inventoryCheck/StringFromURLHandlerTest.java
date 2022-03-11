package com.sunrise.inventoryCheck;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    private ExecutorService executorService;
    @Mock
    private RepositoryCallBack callBack;

    @Before
    public void setUp() throws MalformedURLException {
        executorService = Executors.newSingleThreadExecutor();
        httpHandler = mock(HttpHandler.class);
        stringFromURLHandler = new StringFromURLHandler(httpHandler, executorService);
        stringFromURLHandler.setURLString(DEFAULT_URL_STRING);
        stringFromURLHandler.setBackUpURLString(DEFAULT_BACKUP_URL_STRING);
        url = new URL(DEFAULT_URL_STRING);
        callBack = mock(RepositoryCallBack.class);
        when(httpHandler.makeServiceCall()).thenReturn(JSON_STRING);
    }

    @Test
    public void getStringFromURL_CallsWithUrlOnlyWhenParamIsNull() throws MalformedURLException, InterruptedException {
        stringFromURLHandler.getStringFromURL(null, callBack);
        waitToShutDownExecutorService();
        verify(httpHandler).setUrl(new URL(DEFAULT_URL_STRING));
    }

    private void waitToShutDownExecutorService() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    public void getStringFromURL_ThrowExceptionWhenParamIsNonNumeric() throws InterruptedException {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The string is non-numeric. Please make sure all are numbers");
        stringFromURLHandler.getStringFromURL("123as23acs", null);
        waitToShutDownExecutorService();
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_MakeServiceCall() throws InterruptedException {
        stringFromURLHandler.getStringFromURL(null, null);
        waitToShutDownExecutorService();
        verify(httpHandler).makeServiceCall();
    }

    @Test
    public void getStringFromURL_CallsHttpHandler() throws InterruptedException {
        stringFromURLHandler.getStringFromURL(callBack);
        waitToShutDownExecutorService();
        verify(httpHandler).setUrl(url);
    }

    @Test
    public void getStringFromURL_CallsHttpHandler_Correctly() throws MalformedURLException, InterruptedException {
        stringFromURLHandler.setURLString(DEFAULT_URL_STRING);
        stringFromURLHandler.getStringFromURL("12345", callBack);
        waitToShutDownExecutorService();
        verify(httpHandler).setUrl(new URL(DEFAULT_URL_STRING + "12345"));
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsNull() throws InterruptedException {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The API URL is null");
        stringFromURLHandler.setURLString(null);
        stringFromURLHandler.getStringFromURL(null, callBack);
        waitToShutDownExecutorService();
    }

    @Test
    public void getStringFromURL_ThrowsErrorMessageWhenUrlIsInvalid() throws InterruptedException {
        thrown.expect(InventoryAppException.class);
        thrown.expectMessage("The API URL is invalid");
        stringFromURLHandler.setURLString("invalidInvalidInvalid");
        stringFromURLHandler.getStringFromURL("12445", callBack);
        waitToShutDownExecutorService();
    }

    @Test
    public void getStringFromURL_UsesBackupURLWhenURLReturnsNullString() throws MalformedURLException, InterruptedException {
        when(httpHandler.makeServiceCall()).thenReturn(null);
        stringFromURLHandler.getStringFromURL(callBack);
        waitToShutDownExecutorService();
        verify(httpHandler).setUrl(new URL(DEFAULT_URL_STRING));
        verify(httpHandler).setUrl(new URL(DEFAULT_BACKUP_URL_STRING));
    }
}