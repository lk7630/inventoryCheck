package com.sunrise.inventoryCheck;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.sunrise.inventoryCheck.ErrorMessage.InvalidUrl;
import static com.sunrise.inventoryCheck.ErrorMessage.NullUrl;

public class StringFromURLHandler {

    private String jsonStr;
    private String URLString;
    private String backUpURL;
    private HttpHandler httpHandler;
    private UrlVerifier urlVerifier;

    public StringFromURLHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public void setHttpHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setURLString(String URLString) {
        this.URLString = URLString;
    }

    public String getURLString() {
        return URLString;
    }

    public void setBackUpURL(String backUpURL) {
        this.backUpURL = backUpURL;
    }

    public String getStringFromURL() {
        return returnJsonString(convertStringToURL(URLString));
    }

    public String getStringFromURL(String param) {

        return returnJsonString(convertStringToURL(param));
    }

    private String returnJsonString(URL fullUrl) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            jsonStr = returnString(fullUrl);
            jsonStr = backUpURL != null && jsonStr == null ?
                    returnString(fullUrl) : jsonStr;
        });
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(3, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    private URL convertStringToURL(String reqURL)  {
        if (URLString == null) {
            throw new InvalidURL(NullUrl.getErrorMessage());
        }
        urlVerifier = new UrlVerifier();
        if (!urlVerifier.isValidUrlString(URLString)) {
            throw new InvalidURL(InvalidUrl.getErrorMessage());
        }
        try {
            return new URL(reqURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String returnString(URL url) {
        httpHandler.setUrl(url);
        return httpHandler.makeServiceCall();
    }
}
