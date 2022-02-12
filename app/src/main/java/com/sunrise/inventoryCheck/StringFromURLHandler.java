package com.sunrise.inventoryCheck;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StringFromURLHandler {

    private String jsonStr;
    private String URLString;
    private String backUpURL;
    private HttpHandler httpHandler;

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

    public void setBackUpURL(String backUpURL) {
        this.backUpURL = backUpURL;
    }

    public String getStringFromURL(String param) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            jsonStr = returnString(URLString + param);
            jsonStr = backUpURL != null && jsonStr == null ?
                    returnString(backUpURL + param) : jsonStr;
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

    private String returnString(String string) {
        httpHandler.setUrlFromString(string);
        return httpHandler.makeServiceCall();
    }
}
