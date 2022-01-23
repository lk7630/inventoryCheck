package com.example.jsontest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private String jsonStr;
    ArrayList<HashMap<String, String>> orders;
    HashMap<Object, Object> jsonHashMap;
    private StringFromURLHandler stringFromURLHandler = new StringFromURLHandler();
    private JsonHandler jsonHandler= new JsonHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connectButton = (Button) findViewById(R.id.button);
        EditText bcPanIDTextView = (EditText) findViewById(R.id.editTextNumber);
//        connectButton.setOnClickListener(v -> {
//            jsonStr = retrieveJson(bcPanIDTextView.getText().toString());
//            System.out.println(jsonStr);
//        });
        stringFromURLHandler.setURL("http://websunrise1:10081/plastic/GetLotInfo/261378");
        stringFromURLHandler.setBackUpURL("http://192.168.168.8:10081/plastic/GetLotInfo/261378");
        stringFromURLHandler.getStringFromURL("aa");
        jsonStr=stringFromURLHandler.getJsonStr();
        Log.e("log",jsonStr);
        jsonHashMap=jsonHandler.getHashMapFromJson(jsonStr);
        Log.e("log",jsonStr);

    }

}