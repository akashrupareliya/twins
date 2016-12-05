package com.twins.bringme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Created by Akash Rupareliya on 11/16/2016.
 */

//Class to scan the barcode or QR code
public class ScanBarcodeActivity extends AppCompatActivity {
    SurfaceView cameraPreview;  //Initialize the variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode); //Set layout

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview); //initialization
        createCameraSource();
    }

    /* add click event to the Skip button */
    public void skipscan(View v){
        Intent intent = new Intent(this,ProductDetails.class);
        startActivityForResult(intent,0);
        finish();
    }


    private void createCameraSource() {

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)  //Turn on Auto Focus for scanning
                .setRequestedPreviewSize(1600, 1024) //Size of Barcode or camera display
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                cameraSource.stop();    //Stop Camera/Barcode Scanning
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                //Check if barcode is found or not
                if (barcodes.size()>0){
                    Intent intent = new Intent(getApplicationContext(), ProductDetails.class);
                    intent.putExtra("barcode",barcodes.valueAt(0).displayValue.toString()); //get the latest barcode from the array
                    setResult(CommonStatusCodes.SUCCESS,intent);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    //Menu item for help
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.help_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Click event to go to Help Section
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        Intent intent = new Intent(this,help.class);
        startActivityForResult(intent,0);
        return super.onOptionsItemSelected(item);
    }
}