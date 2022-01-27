package com.example.jsontest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static android.graphics.Color.RED;
import static com.example.jsontest.BarcodeScanActivity.BARCODE_KEY;

public class MainActivity extends AppCompatActivity {
    private String jsonStr;
    HashMap<Object, Object> jsonHashMap;
    private JsonHandler jsonHandler = new JsonHandler();
    private RecyclerView.Adapter listAdapter;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public  EditText bcPanIDTextView;
    TextView folderView;
    TextView lotView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button scanButton = (Button) findViewById(R.id.scanButton);
        bcPanIDTextView = (EditText) findViewById(R.id.editTextNumber);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        folderView = (TextView) findViewById(R.id.folderView);
        lotView = (TextView) findViewById(R.id.lotView);

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
                            jsonStr = returnStringFromAPI(resultText);
                            jsonHashMap = jsonHandler.getHashMapFromJson(jsonStr);
                            List<HashMap<Object, Object>> jsonList = jsonHandler.getLotItemList();
                            displayLot(jsonHashMap);
                            displayList((jsonList));
                        }
                    }
                });

        scanButton.setOnClickListener(v -> {
            bcPanIDTextView.setText("");
            Intent scanIntent = new Intent(this, BarcodeScanActivity.class);
            scanActivityLauncher.launch(scanIntent);
        });

        //todo - remove
//        jsonStr = "{\"panID\":\"180310\",\"folder\":\"RGPP\",\"lot\":\"2210028\",\"lotItems\"" +
//                ":[{\"polymer\":\"PP\",\"form\":\"RG\",\"packs\":\"9\",\"packing\":" +
//                "\"super sack\",\"weight\":\"10800\",\"warehouse\":\"14\"},{\"polymer" +
//                "\":\"PP\",\"form\":\"RG\",\"packs\":\"2\",\"packing\":\"super sack\"," +
//                "\"weight\":\"2400\",\"warehouse\":\"9\"}]}";
    }

    private String processBarcode(String barcode) {
        String stringPattern = "^PAN/\\d{6,}";//start with PAN/ and has min of 6 numbers
        Pattern pattern = Pattern.compile(stringPattern);
        boolean isValid = pattern.matcher(barcode).matches();
        return isValid ? barcode.replaceAll("PAN/", "").trim()
                : "wrong barcode!";
    }

    private String returnStringFromAPI(String bcPanID) {
        StringFromURLHandler stringFromURLHandler = new StringFromURLHandler();
        stringFromURLHandler.setURL("http://192.168.168.8:10081/plastic/GetLotInfo/");
        stringFromURLHandler.setBackUpURL("http://websunrise1:10081/plastic/GetLotInfo/");
        return stringFromURLHandler.getStringFromURL(bcPanID);
    }


    private void displayLot(HashMap<Object, Object> jsonHashMap) {
        folderView.setText(jsonHashMap.get("folder").toString() + "   -- ");
        lotView.setText(jsonHashMap.get("lot").toString());
    }

    private void displayList(List<HashMap<Object, Object>> jsonList) {
        Collections.sort(jsonList, new CustomArraySort("warehouse"));
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ViewAdapter(jsonList);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
    }

}