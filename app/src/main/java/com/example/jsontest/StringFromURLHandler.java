package com.example.jsontest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StringFromURLHandler {

    private String jsonStr;
    private String URL;
    private String backUpURL;
    private HttpHandler httpHandler =new HttpHandler();


    public String getJsonStr() {
        return jsonStr;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setBackUpURL(String backUpURL) {
        this.backUpURL = backUpURL;
    }

    public void getStringFromURL(String param) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            jsonStr = httpHandler.makeServiceCall(URL);
            if (jsonStr == null && backUpURL != null){
                jsonStr = httpHandler.makeServiceCall(backUpURL);
            }
        });
    }
}
