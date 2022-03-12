package com.sunrise.inventoryCheck;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.Executor;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();
    private URL url;

    public void setUrl(URL url) {
            this.url = url;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrlFromString(String reqURL) {
        try {
            this.url = new URL(reqURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String makeServiceCall() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(2000);
                    Log.e("response: ",conn.getResponseMessage());
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    return convertStreamToString(in);
                } catch (SocketTimeoutException exception){
                    Log.e("Error: ","Connection timeout after 2s");
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
