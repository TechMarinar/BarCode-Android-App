/*
 * MIT License
 *
 * Copyright (c) 2021 Mohamed Ahmed Dahab
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.techmarinar.bestbarcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * programmed By Mohamed Ahmed Dahab 23/january/2021
 * **/

public class BarCodeScanner extends AppCompatActivity {

    private static final String TAG = "BarCodeScanner";
    //scanner object
    private CodeScanner mCodeScanner;
    //widget
    private CodeScannerView scannerViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);

        //find the widget [scannerView]
        setUpWidget();

        //start scanning the barCodes
        startBarCodeScanner();

    }

    private void setUpWidget() {
        //set title
        setTitle("Scan BarCodes");

        //find the scannerView
         scannerViewer = (CodeScannerView) findViewById(R.id.scanner_view);
    }

    /**
     * the codes inside this method is from the official Library website :
     * [https://github.com/yuriy-budiyev/code-scanner]
     * **/
    private void startBarCodeScanner() {

        mCodeScanner = new CodeScanner(this, scannerViewer);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarCodeScanner.this, result.getText(), Toast.LENGTH_SHORT).show();

                        //if you want to close this activity after you get the result directly
                        //then un comment the below call [finish();]
                        //finish();
                    }
                });
            }
        });

        /**********************************[ClickListener]************************************/
        //set ClickListener to ScannerViewer
        scannerViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();

            }
        });

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check for cameraPermission
        checkCameraPermission();

    }

    /********
     * dexter runtime permission library
     * website [https://github.com/Karumi/Dexter]
     * *******/
    private void checkCameraPermission() {

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //start the preview
                        mCodeScanner.startPreview();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(BarCodeScanner.this, "camera Permissions is Required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken token) {

                        token.continuePermissionRequest();

                    }
                })

                .check();
    }
}