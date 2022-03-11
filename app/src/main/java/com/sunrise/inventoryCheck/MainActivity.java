package com.sunrise.inventoryCheck;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;
import static android.graphics.Color.rgb;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.LayoutManager;
import static com.sunrise.inventoryCheck.BarcodeScanActivity.BARCODE_KEY;
import static java.util.Arrays.asList;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String WEB_API_URL = "http://38.122.193.242:10081/plastic/GetLotInfo/";
    public static final String LOCAL_API_URL = "http://192.168.168.8:10081/plastic/GetLotInfo/";
    private String jsonStr;
    private HashMap<Object, Object> jsonHashMap;
    private final JsonHandler jsonHandler = new JsonHandler();
    private RecyclerView recyclerView;
    private LayoutManager layoutManager;
    public TextView bcPanIDTextView;
    private TextView folderView;
    private TextView totalWeightView;
    private Spinner sortSpinner;
    private List<HashMap<Object, Object>> jsonList;
    private TextView ascText;
    private TextView dscText;
    private boolean isDescOrder;
    private String sortKey;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanButton = (Button) findViewById(R.id.scanButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        bcPanIDTextView = (TextView) findViewById(R.id.editTextNumber);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        folderView = (TextView) findViewById(R.id.folderView);
        totalWeightView = (TextView) findViewById(R.id.totalWeightView);
        sortSpinner = (Spinner) findViewById(R.id.spinner);
        ascText = (TextView) findViewById(R.id.ascText);
        dscText = (TextView) findViewById(R.id.dscText);
        isDescOrder = false;

        //back from Activity
        ActivityResultLauncher<Intent> scanActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK
                            || result.getResultCode() == RESULT_FIRST_USER) {
                        String barcodeText = result.getData().getStringExtra(BARCODE_KEY);
                        if (result.getResultCode() == RESULT_FIRST_USER) {
                            barcodeText = "PAN/" + barcodeText;
                        }
                        String resultText = processBarcode(barcodeText);
                        if (resultText.equals("wrong barcode")) {
                            bcPanIDTextView.setTextColor(RED);
                            bcPanIDTextView.setText("wrong barcode !");
                        } else {
                            bcPanIDTextView.setText(resultText);
                            progressBar.setVisibility(VISIBLE);
                            returnStringFromAPI(resultText);
                        }
                    }
                });

        scanButton.setOnClickListener(v -> {
            bcPanIDTextView.setText("");
            Intent scanIntent = new Intent(this, BarcodeScanActivity.class);
            scanActivityLauncher.launch(scanIntent);
        });


        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortKey = parent.getItemAtPosition(position).toString();
                displayList(jsonList, sortKey, isDescOrder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ascText.setOnClickListener(v -> {
            isDescOrder = false;
            ascText.setTypeface(ascText.getTypeface(), Typeface.ITALIC);
            ascText.setTextColor(rgb(255, 165, 0));
            dscText.setTypeface(dscText.getTypeface(), Typeface.NORMAL);
            dscText.setTextColor(GRAY);
            displayList(jsonList, sortKey, isDescOrder);
        });

        dscText.setOnClickListener(v -> {
            isDescOrder = true;
            dscText.setTypeface(dscText.getTypeface(), Typeface.ITALIC);
            dscText.setTextColor(rgb(255, 165, 0));
            ascText.setTypeface(ascText.getTypeface(), Typeface.NORMAL);
            ascText.setTextColor(GRAY);
            displayList(jsonList, sortKey, isDescOrder);
        });

        Log.e("aaaa", String.valueOf("super sack".compareTo("box")));
        Log.e("aaaa", String.valueOf("HDPE".compareTo("PP")));
        List<String> sortArrayList = asList("warehouse", "polymer", "packing");
        loadSpinner(sortArrayList);
    }

    private String processBarcode(String barcode) {
        String stringPattern = "^PAN/\\d{6,}";//start with PAN/ and has min of 6 numbers
        Pattern pattern = Pattern.compile(stringPattern);
        boolean isValid = pattern.matcher(barcode).matches();
        return isValid ? barcode.replaceAll("PAN/", "").trim()
                : "wrong barcode!";
    }

    private void returnStringFromAPI(String bcPanID) {
        StringFromURLHandler stringFromURLHandler = new StringFromURLHandler(new HttpHandler(),
                Executors.newSingleThreadExecutor());
        stringFromURLHandler.setURLString(LOCAL_API_URL);
        stringFromURLHandler.setBackUpURLString(WEB_API_URL);
        stringFromURLHandler.getStringFromURL(bcPanID, new RepositoryCallBack() {
            @Override
            public void onComplete(String result) {
                Log.e("from callBack", result);
                jsonStr = result;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        updateLotInfo();
                    }
                });
            }
        });

        Log.e("afafa", "addadad");
    }

    private void updateLotInfo() {
        jsonHashMap = jsonHandler.getHashMapFromJson(jsonStr);
        jsonList = jsonHandler.getLotItemList();
        displayLot(jsonHashMap);
        displayTotalWeight(jsonHashMap);
        displayList((jsonList), "warehouse", isDescOrder);
        progressBar.setVisibility(GONE);
    }

    private void displayLot(HashMap<Object, Object> jsonHashMap) {
        if (jsonStr == null) {
            folderView.setText("NO INFO");
        } else {
            folderView.setText(String.format("%s - %s", jsonHashMap.get("folder").toString(),
                    jsonHashMap.get("lot").toString()));
        }
    }

    private void displayTotalWeight(HashMap<Object, Object> jsonHashMap) {
        if (jsonStr == null) {
            totalWeightView.setText("");
        } else if (jsonHashMap.get("lotItems") == null || jsonHashMap.get("lotItems").equals("")) {
            totalWeightView.setText("Total weight: 0");
        } else {
            ArrayList<HashMap<Object, Object>> lotItemArray = (ArrayList<HashMap<Object, Object>>)
                    jsonHashMap.get("lotItems");
            int sum = 0;
            for (int i = 0; i < lotItemArray.size(); i++) {
                sum += Integer.parseInt((String) lotItemArray.get(i).get("weight"));
            }
            totalWeightView.setText("Total: \n\t" + sum + " lbs");
        }
    }

    private void displayList(List<HashMap<Object, Object>> jsonList, String sortKey, boolean isDescOrder) {
        if (jsonStr != null) {
            Collections.sort(jsonList, new CustomArraySort(sortKey, isDescOrder));
            layoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(layoutManager);
            Adapter listAdapter = new ViewAdapter(jsonList);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }

    private void loadSpinner(List<String> sortArrayList) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, sortArrayList);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setSelection(0);
        sortKey = sortArrayList.get(0);
    }

}