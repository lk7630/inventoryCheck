package com.sunrise.inventoryCheck;

import static com.sunrise.inventoryCheck.enums.ErrorMessage.InvalidUrl;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NonNumericString;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NullUrl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFromURLHandler {

    private String jsonStr;
    private String URLString;
    private String backUpURLString;
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

    public void setBackUpURLString(String backUpURLString) {
        this.backUpURLString = backUpURLString;
    }

    public String getStringFromURL() {
        return returnJsonString(null);
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
        return returnJsonString(param);
    }

    private String returnJsonString(String param) {
        int timesToTry = 2;
        URL url = param == null ? convertStringToURL(URLString)
                : convertStringToURL(URLString + param);
        URL backupUrl = param == null ? convertStringToURL(backUpURLString)
                : convertStringToURL(backUpURLString + param);
        while (timesToTry > 0) {
            timesToTry -= 1;
            jsonStr = returnString(url);
            if (jsonStr == null) {
                jsonStr = returnString(backupUrl);
            }
            if (jsonStr != null) {
                return jsonStr;
            }
        }
        return null;
    }

    private String returnString(URL url) {
        httpHandler.setUrl(url);
        return httpHandler.makeServiceCall();
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
}
