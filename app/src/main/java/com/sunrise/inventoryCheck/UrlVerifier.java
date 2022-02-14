package com.sunrise.inventoryCheck;

public class UrlVerifier {

    public UrlVerifier() {
    }

    public boolean isValidUrlString(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        return isHttpUrlString(url) || isHttpsUrlString(url);
    }

    private boolean isHttpUrlString(String url) {
        return (null != url) &&
                (url.length() > 6) &&
                url.substring(0, 7).equalsIgnoreCase("http://");
    }

    private boolean isHttpsUrlString(String url) {
        return (null != url) &&
                (url.length() > 7) &&
                url.substring(0, 8).equalsIgnoreCase("https://");
    }

}
