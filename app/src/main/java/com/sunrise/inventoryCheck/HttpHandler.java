package com.sunrise.inventoryCheck;

import static com.sunrise.inventoryCheck.enums.CustomResponse.ConnectionTimeout;
import static com.sunrise.inventoryCheck.enums.CustomResponse.Empty_Content;
import static com.sunrise.inventoryCheck.enums.CustomResponse.ReadSuccess;

import android.util.Log;

import com.sunrise.inventoryCheck.enums.CustomResponse;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class HttpHandler {

    private URL url;
    private String downloadContent;

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public String getDownloadContent() {
        return downloadContent;
    }

    public void setUrlFromString(String reqURL) {
        try {
            this.url = new URL(reqURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public CustomResponse makeServiceCall() {
        downloadContent = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            Log.e("response: ", conn.getResponseMessage());
            //todo add logging service here to log connection_ok
            InputStream in = new BufferedInputStream(conn.getInputStream());
            downloadContent = convertStreamToString(in);
            return downloadContent.isEmpty() ? Empty_Content : ReadSuccess;
        } catch (SocketTimeoutException exception) {
            Log.e("Error: ", "Connection timeout after 2s");
            return ConnectionTimeout;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
