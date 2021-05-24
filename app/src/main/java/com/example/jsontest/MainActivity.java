package com.example.jsontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String jsonStr;
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GetInfo().execute();
    }

    private class GetInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh=new HttpHandler();
           // String url="http://38.122.193.242:10081/container";
            String url="http://websunrise1:10081/CONTAINER";
            jsonStr=sh.makeServiceCall(url);

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i("okkk",jsonStr);

        }
    }
}