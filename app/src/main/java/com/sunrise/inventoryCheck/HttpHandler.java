package com.sunrise.inventoryCheck;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class HttpHandler {
    private static final String TAG = HttpHandler.class.getSimpleName();
    private URL url;

    public HttpHandler() {
    }

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
        CompletableFuture<String> completableFuture= CompletableFuture.supplyAsync((Supplier<String>) () -> {
            try {
                HttpURLConnection conn;
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                return convertStreamToString(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        try {
            return completableFuture.get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
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
