package com.example.jsontest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String jsonStr;
    ArrayList<HashMap<String,String>> orders;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orders=new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);
        new GetInfo().execute();
    }

    private class GetInfo extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh=new HttpHandler();
           // String url="http://38.122.193.242:10081/container";
            String url="http://websunrise1:10081/CONTAINER";
            jsonStr=sh.makeServiceCall(url);
            if (jsonStr!=null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        String reference = o.getString("reference");
                        String salesOrder = o.getString("salesOrder");
                        String lot = o.getString("lot");
                        String note = o.getString("note");

                        HashMap<String, String> tmpHashmap = new HashMap<>();
                        tmpHashmap.put("reference", reference);
                        tmpHashmap.put("salesOrder", salesOrder);
                        tmpHashmap.put("lot", lot);
                        tmpHashmap.put("note", note);

                        orders.add(tmpHashmap);

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, orders,R.layout.list_item,new String[]{"reference","salesOrder"},new int[]{R.id.email,R.id.mobile});
            lv.setAdapter(adapter);
        }
    }
}