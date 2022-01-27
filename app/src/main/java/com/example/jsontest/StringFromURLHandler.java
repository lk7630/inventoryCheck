package com.example.jsontest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StringFromURLHandler {

    private String jsonStr;
    private String URL;
    private String backUpURL;
    private HttpHandler httpHandler = new HttpHandler();


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
            jsonStr = (param == null || param.isEmpty()) ? httpHandler.makeServiceCall(URL)
                    : httpHandler.makeServiceCall(URL + param);
            if (jsonStr == null && backUpURL != null) {
                jsonStr = (param == null || param.isEmpty())
                        ? httpHandler.makeServiceCall(backUpURL)
                        : httpHandler.makeServiceCall(backUpURL + param);
            }
        });
        return jsonStr;
    }
}
