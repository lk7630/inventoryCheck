package com.sunrise.inventoryCheck;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sunrise.inventoryCheck.enums.ErrorMessage.InvalidUrl;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NonNumericString;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NullUrl;

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
        if (param == null) {
            return getStringFromURL();
        }
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(param);
        if (!matcher.matches()) {
            throw new InventoryAppException(NonNumericString.getErrorMessage());
        }
        return returnJsonString(convertStringToURL(URLString + param));
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

    private URL convertStringToURL(String reqURL) {
        if (URLString == null) {
            throw new InventoryAppException(NullUrl.getErrorMessage());
        }
        urlVerifier = new UrlVerifier();
        if (!urlVerifier.isValidUrlString(URLString)) {
            throw new InventoryAppException(InvalidUrl.getErrorMessage());
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
