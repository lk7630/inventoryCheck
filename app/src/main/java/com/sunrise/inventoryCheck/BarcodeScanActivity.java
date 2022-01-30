package com.sunrise.inventoryCheck;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

// using CameraX to scan barcode
public class BarcodeScanActivity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    public static String BARCODE_KEY = "barcode";

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    //ImageCapture imageCapture;
    Button button;
    String barcodeText;
    ProcessCameraProvider cameraProvider;
    ImageAnalysis imageAnalysis;
    Button flashToggleButton;
    Button manualScanButton;
    Camera camera;
    boolean isFlashOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        //check permission and request if no permission

        if (!hasCameraPermission()) {
            requestPermission();
        }

        isFlashOn = false;
        previewView = findViewById(R.id.previewView);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = width / 3 * 4;
        previewView.setLayoutParams(new PreviewView.LayoutParams(width, height));
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        button = (Button) findViewById(R.id.scanButton);
        flashToggleButton = findViewById(R.id.flashToggleButton);
        manualScanButton = findViewById(R.id.manualButton);
        Button backButton = findViewById(R.id.back1Button);


        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));

        flashToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFlashOn) {
                    flashToggleButton.setText("Flash off");
                    isFlashOn = true;
                    if (camera.getCameraInfo().hasFlashUnit()) {
                        camera.getCameraControl().enableTorch(true);
                    }
                } else {
                    flashToggleButton.setText("Flash on");
                    isFlashOn = false;
                    if (camera.getCameraInfo().hasFlashUnit()) {
                        camera.getCameraControl().enableTorch(false);
                    }
                }

            }
        });

        manualScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manualInput();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageAnalysis =
                new ImageAnalysis.Builder()
                        //.setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        //.setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)//keep the latest
                        //to prevent bottleneck
                        .build();

        BarcodeScannerOptions barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(// input all desired code formats to be scanned
                        Barcode.FORMAT_CODE_128
                        //Barcode.FORMAT_CODE_39
                ).build();
        BarcodeScanner scanner = BarcodeScanning.getClient(barcodeScannerOptions);

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
                Image mediaImage = imageProxy.getImage();
                if (mediaImage != null) {
                    InputImage image = InputImage.fromMediaImage(mediaImage,
                            imageProxy.getImageInfo().getRotationDegrees());

                    Task<List<Barcode>> result = scanner.process(image);// get a list of barcode
                    result.addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            processBarcode(barcodes);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not detect barcode!"
                                    , Toast.LENGTH_SHORT).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            mediaImage.close();
                            imageProxy.close();
                        }
                    });
                }
            }
        });


        cameraProvider.unbindAll();
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    private void manualInput() {
        cameraProvider.unbindAll();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("MANUAL INPUT");
        builder.setMessage("The number is on the top right of the label/paper.\nPlease type in the NUMBER part only");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.requestFocus();
        builder.setView(input);

        //Show keyboard automatically
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                barcodeText = input.getText().toString().toUpperCase();
                if (barcodeText != null) {
                    Intent intent = new Intent();
                    intent.putExtra(BARCODE_KEY, barcodeText);
                    setResult(RESULT_FIRST_USER, intent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        builder.show().setCanceledOnTouchOutside(false);
    }

    // get a list of barcodes but only process to first one to avoid complexity
    private void processBarcode(List<Barcode> barcodes) {
        Log.e("barcodes:", String.valueOf(barcodes));
        for (Barcode barcode : barcodes) {
            // final Rect boundingBox=barcode.getBoundingBox();
            // final Point[] cornerPoints=barcode.getCornerPoints();
            String displayValue = barcode.getDisplayValue();
            int valueType = barcode.getValueType();
            if (valueType == Barcode.TYPE_TEXT) {
                barcodeText = displayValue;
                cameraProvider.unbindAll();
                if (barcodeText != null) {
                    Intent intent = new Intent();
                    intent.putExtra(BARCODE_KEY, barcodeText);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        }
    }

    /////handle cameraX permission////////////
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
    //////////////////////
}