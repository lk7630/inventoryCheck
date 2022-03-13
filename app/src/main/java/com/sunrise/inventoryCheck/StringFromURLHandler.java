package com.sunrise.inventoryCheck;

import static com.sunrise.inventoryCheck.enums.CustomResponse.ReadSuccess;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.InvalidUrl;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NonNumericString;
import static com.sunrise.inventoryCheck.enums.ErrorMessage.NullUrl;

import android.util.Log;

import com.sunrise.inventoryCheck.enums.CustomResponse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFromURLHandler {

    private String jsonStr;
    private String URLString;
    private CustomResponse response;
    private String backUpURLString;
    private HttpHandler httpHandler;
    private final ExecutorService executorService;

    public StringFromURLHandler(HttpHandler httpHandler, ExecutorService executorService) {
        this.httpHandler = httpHandler;
        this.executorService = executorService;
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

    public void getStringFromURL(RepositoryCallBack callBack) {
        returnJsonString(null, callBack);
    }

    public void getStringFromURL(String param, RepositoryCallBack callBack) {
        if (param == null) {
            getStringFromURL(callBack);
        } else {
            Pattern pattern = Pattern.compile("^\\d+$");
            Matcher matcher = pattern.matcher(param);
            if (!matcher.matches()) {
                throw new InventoryAppException(NonNumericString.getErrorMessage());
            }
            returnJsonString(param, callBack);
        }
    }

    private void returnJsonString(String param, RepositoryCallBack callBack) {
        URL url = param == null ? convertStringToURL(URLString)
                : convertStringToURL(URLString + param);
        URL backupUrl = param == null ? convertStringToURL(backUpURLString)
                : convertStringToURL(backUpURLString + param);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                jsonStr = returnString(url, false);
                if (jsonStr == null) {
                    jsonStr = returnString(backupUrl, true);
                }
                if (jsonStr!=null) {
                    Log.e("Return string:", jsonStr);
                }
                callBack.onReadComplete(jsonStr, response);
            }
        });
    }

    private String returnString(URL url, boolean isBackupURL) {
        httpHandler.setUrl(url);
        response = httpHandler.makeServiceCall();
        if (!isBackupURL) {
            return response == ReadSuccess ? httpHandler.getDownloadContent() : null;
        }
        return response == ReadSuccess ? httpHandler.getDownloadContent() : "";
    }

    private URL convertStringToURL(String reqURL) {
        if (URLString == null) {
            throw new InventoryAppException(NullUrl.getErrorMessage());
        }
        UrlVerifier urlVerifier = new UrlVerifier();
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
