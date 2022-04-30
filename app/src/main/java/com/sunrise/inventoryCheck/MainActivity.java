package com.sunrise.inventoryCheck;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.RED;
import static android.graphics.Color.rgb;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.LayoutManager;
import static com.sunrise.inventoryCheck.BarcodeScanActivity.BARCODE_KEY;
import static com.sunrise.inventoryCheck.enums.ViewState.LastInventoryCount;
import static com.sunrise.inventoryCheck.enums.ViewState.LotInfo;
import static java.util.Arrays.asList;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sunrise.inventoryCheck.enums.CustomResponse;
import com.sunrise.inventoryCheck.enums.ViewState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String WEB_GET_LOT_URL = "http://38.122.193.242:10081/plastic/GetLotInfo/";
    public static final String LOCAL_GET_LOT_URL = "http://192.168.168.8:10081/plastic/GetLotInfo/";
    public static final String WEB_GET_FOLDER_URL = "http://38.122.193.242:10081/plastic/GetFolderList/";
    public static final String LOCAL_GET_FOLDER_URL = "http://192.168.168.8:10081/plastic/GetFolderList/";
    public static final String WEB_GET_INVENTORY_COUNT_URL = "http://38.122.193.242:10081/plastic/GetInventoryCountInfo/";
    public static final String LOCAL_GET_INVENTORY_COUNT_URL = "http://192.168.168.8:10081/plastic/GetInventoryCountInfo/";
    private String jsonStr;
    private final JsonHandler jsonHandler = new JsonHandler();
    private RecyclerView recyclerView;
    public TextView statusView;
    private TextView folderView;
    private TextView totalWeightView;
    private Spinner sortSpinner;
    private TextView ascText;
    private TextView dscText;
    private boolean isDescOrder;
    private String sortKey;
    private ProgressBar progressBar;
    private Button scanButton;
    private Button inputButton;
    private LotSystemInventory lotSystemInventory;
    private LinearLayout lotInfoHeader;
    private LinearLayout lastInventoryHeader;
    private ViewState viewState;
    private Button switchButton;
    private TextView title;
    private List<LastInventory> lastInventories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewState = LastInventoryCount;
        findViews();
        isDescOrder = false;
        LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        lotSystemInventory = new LotSystemInventory();
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
                            statusView.setTextColor(RED);
                            statusView.setText("wrong barcode !");
                        } else {
                            statusView.setText(resultText);
                            progressBar.setVisibility(VISIBLE);
                            returnStringFromAPI(resultText, asList(LOCAL_GET_LOT_URL,
                                    WEB_GET_LOT_URL),
                                    callBackFromGetLotInfo);
                        }
                    }
                });
        buildListeners(scanActivityLauncher);

    }

    private void buildListeners(ActivityResultLauncher<Intent> scanActivityLauncher) {
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortKey = parent.getItemAtPosition(position).toString();
                displayLotInfo(lotSystemInventory.getLotItems(), sortKey, isDescOrder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        List<String> sortArrayList = asList("warehouse", "polymer", "packing", "weight");
        loadSpinner(sortArrayList);
        ascText.setOnClickListener(v -> {
            isDescOrder = false;
            ascText.setTypeface(ascText.getTypeface(), Typeface.ITALIC);
            ascText.setTextColor(rgb(255, 165, 0));
            dscText.setTypeface(dscText.getTypeface(), Typeface.NORMAL);
            dscText.setTextColor(GRAY);
            displayLotInfo(lotSystemInventory.getLotItems(), sortKey, isDescOrder);
        });

        dscText.setOnClickListener(v -> {
            isDescOrder = true;
            dscText.setTypeface(dscText.getTypeface(), Typeface.ITALIC);
            dscText.setTextColor(rgb(255, 165, 0));
            ascText.setTypeface(ascText.getTypeface(), Typeface.NORMAL);
            ascText.setTextColor(GRAY);
            displayLotInfo(lotSystemInventory.getLotItems(), sortKey, isDescOrder);
        });
        scanButton.setOnClickListener(v -> {
            statusView.setText("");
            Intent scanIntent = new Intent(this, BarcodeScanActivity.class);
            scanActivityLauncher.launch(scanIntent);
        });

        inputButton.setOnClickListener(v -> {
            progressBar.setVisibility(VISIBLE);
            returnStringFromAPI(null, asList(LOCAL_GET_FOLDER_URL, WEB_GET_FOLDER_URL),
                    callBackFromGetFolderList);
        });

        switchButton.setOnClickListener(view -> {
            if (viewState == LotInfo) {
                viewState = LastInventoryCount;
            } else {
                viewState = LotInfo;
            }
            showItemList(viewState);
        });
    }

    private void findViews() {
        lotInfoHeader = findViewById(R.id.lotInfoHeaderView);
        lotInfoHeader.addView(getLayoutInflater().inflate(R.layout.lot_info_header, lotInfoHeader,
                false));
        lotInfoHeader.setVisibility(GONE);
        lastInventoryHeader = findViewById(R.id.lastInventoryHeaderView);
        lastInventoryHeader.addView(getLayoutInflater().inflate(R.layout.last_inventory_header,
                lastInventoryHeader, false));
        lastInventoryHeader.setVisibility(GONE);
        sortSpinner = findViewById(R.id.spinner);
        ascText = findViewById(R.id.ascText);
        dscText = findViewById(R.id.dscText);
        scanButton = findViewById(R.id.scanButton);
        inputButton = findViewById(R.id.inputButton);
        switchButton = findViewById(R.id.switchButton);
        progressBar = findViewById(R.id.progressBar);
        statusView = findViewById(R.id.editTextNumber);
        recyclerView = findViewById(R.id.recyclerView);
        folderView = findViewById(R.id.folderView);
        totalWeightView = findViewById(R.id.totalWeightView);
        title = findViewById(R.id.titleView);
    }

    private String processBarcode(String barcode) {
        String stringPattern = "^PAN/\\d{6,}";//start with PAN/ and has min of 6 numbers
        Pattern pattern = Pattern.compile(stringPattern);
        boolean isValid = pattern.matcher(barcode).matches();
        return isValid ? barcode.replaceAll("PAN/", "").trim()
                : "wrong barcode!";
    }

    private void returnStringFromAPI(String param, List<String> urls,
                                     RepositoryCallBack callBack) {
        StringFromURLHandler stringFromURLHandler = new StringFromURLHandler(new HttpHandler(),
                Executors.newSingleThreadExecutor());
        stringFromURLHandler.setURLString(urls.get(0));
        if (urls.size() > 1) {
            stringFromURLHandler.setBackUpURLString(urls.get(1));
        }
        stringFromURLHandler.getStringFromURL(param, callBack);
    }

    private final RepositoryCallBack callBackFromGetLotInfo = new RepositoryCallBack() {
        @Override
        public void onReadComplete(String result, CustomResponse response) {
            jsonStr = result;
            new Handler(Looper.getMainLooper()).post(() -> {
                statusView.setText(response.getResponseMessage());
                collectLotInfoAndLastInventory(jsonStr);
            });
        }
    };

    private void collectLotInfoAndLastInventory(String jsonStr) {
        try {
            lotSystemInventory = new ObjectMapper().readValue(jsonStr, LotSystemInventory.class);
        } catch (IOException e) {
            e.printStackTrace();
            //todo throw
        }
        if (lotSystemInventory.getPanID() != null) {
            returnStringFromAPI(lotSystemInventory.getPanID(),
                    asList(LOCAL_GET_INVENTORY_COUNT_URL, WEB_GET_INVENTORY_COUNT_URL),
                    callBackFromLastInventoryCount);
        }
    }

    private void showItemList(ViewState viewState) {
        displayLot(lotSystemInventory);
        if (viewState == LotInfo) {
            title.setText("SYSTEM LOT INFO");
            lastInventoryHeader.setVisibility(GONE);
            lotInfoHeader.setVisibility(VISIBLE);
            displayTotalWeight(lotSystemInventory.getLotItems());
            displayLotInfo((lotSystemInventory.getLotItems()), "warehouse", isDescOrder);
        } else {
            title.setText("LAST INVENTORY COUNT");
            lastInventoryHeader.setVisibility(VISIBLE);
            lotInfoHeader.setVisibility(GONE);
            totalWeightView.setText("");
            displayLastInventory(lastInventories);
        }
    }

    private final RepositoryCallBack callBackFromGetFolderList = new RepositoryCallBack() {
        @Override
        public void onReadComplete(String result, CustomResponse response) {
            jsonStr = result;
            new Handler(Looper.getMainLooper()).post(() -> {
                statusView.setText(response.getResponseMessage());
                progressBar.setVisibility(GONE);
                showDialog();
            });
        }
    };

    private final RepositoryCallBack callBackFromLastInventoryCount = new RepositoryCallBack() {
        @Override
        public void onReadComplete(String result, CustomResponse response) {
            ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            try {
                lastInventories = Arrays.asList(objectMapper.readValue(result, LastInventory[].class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                statusView.setText(response.getResponseMessage());
                progressBar.setVisibility(GONE);
                showItemList(viewState);
            });
        }
    };

    private void displayLot(LotSystemInventory lotSystemInventory) {
        if (jsonStr == null) {
            folderView.setText("NO INFO");
        } else {
            folderView.setText(String.format("%s - %s", lotSystemInventory.getFolder(),
                    lotSystemInventory.getLot()));
        }
    }

    private void displayTotalWeight(List<LotItem> lotItems) {
        if (jsonStr == null) {
            totalWeightView.setText("");
        } else if (lotItems.isEmpty()) {
            totalWeightView.setText("Total weight: 0");
        } else {
            int sum = lotItems.stream().mapToInt(LotItem::getWeight).sum();
            totalWeightView.setText("Total weight: " + sum + " lbs");
        }
    }

    private void displayLotInfo(List<LotItem> lotItems, String sortKey, boolean isDescOrder) {
        if (jsonStr != null) {
            lotItems.sort(new CustomArraySort(sortKey, isDescOrder));
            Adapter listAdapter = new LotInfoViewAdapter(lotItems);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setHasFixedSize(true);
        }
    }

    private void displayLastInventory(List<LastInventory> lastInventories) {
        Adapter listAdapter = new LastInventoryViewAdapter(lastInventories);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void loadSpinner(List<String> sortArrayList) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, sortArrayList);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setSelection(0);
        sortKey = sortArrayList.get(0);
    }

    private void showDialog() {
        HashMap<Object, Object> jsonHash = jsonHandler.getFolderIDList(jsonStr);
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("title");
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.show();

        AutoCompleteTextView typeView = dialog.findViewById(R.id.autoCompleteTextView);
        List<Object> hashValues = asList(jsonHash.keySet().toArray());
        List<String> typeViewValues = new ArrayList<>(hashValues.size());
        hashValues.forEach(object -> typeViewValues.add(object.toString()));

        ArrayAdapter<String> typeViewAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.select_dialog_item, typeViewValues);
        typeView.setAdapter(typeViewAdapter);
        typeView.setThreshold(1);
        EditText lotEditText = dialog.findViewById(R.id.lotEditText);
        Button dialogSubmitButton = dialog.findViewById(R.id.submitButton);
        dialogSubmitButton.setOnClickListener(v -> {
            String folder = jsonHash.get(typeView.getText().toString()).toString();
            statusView.setText(typeView.getText() + "-" + lotEditText.getText().toString());
            getLotInfoByFolderID(folder, lotEditText.getText().toString());
            dialog.dismiss();
        });
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dialog.dismiss());
    }

    private void getLotInfoByFolderID(String folder, String lot) {
        progressBar.setVisibility(VISIBLE);
        returnStringFromAPI(lot, asList(LOCAL_GET_LOT_URL + folder + "/", WEB_GET_LOT_URL +
                folder + "/"), callBackFromGetLotInfo);
    }
}