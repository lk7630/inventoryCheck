package com.example.jsontest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StringFromURLHandler {

    private String jsonStr;
    private String URL;
    private String backUpURL;
    private HttpHandler httpHandler = new HttpHandler();

    public StringFromURLHandler() {
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setBackUpURL(String backUpURL) {
        this.backUpURL = backUpURL;
    }

    public String getStringFromURL(String param) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            jsonStr = httpHandler.makeServiceCall(URL + param);
            jsonStr = backUpURL != null && jsonStr == null ?
                    httpHandler.makeServiceCall(backUpURL + param) : jsonStr;
        });
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
}
