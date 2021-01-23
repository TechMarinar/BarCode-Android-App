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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
/**
 * programmed By Mohamed Ahmed Dahab 23/january/2021
 * **/
public class MainActivity extends AppCompatActivity {
    /**
     * Generate Barcode Activity
     * **/
    private static final String TAG = "MainActivity";
    //widget
    private Button mScaBT, mGenerateBt;
    private EditText mGenerateEDT;
    private ImageView mBarcodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main");
        //permissions
        checkForPermission();
        //set Up Widget
        setUpWidget();
    }

    private void checkForPermission() {

        String permissions[]= new String[]{Manifest.permission.CAMERA
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE };
        //camera Permissions
        if (ActivityCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_DENIED) {

            //request camera permissions
            reQuestRunTimePermission(permissions);

        }else
            //storage permissions
            if (ActivityCompat.checkSelfPermission(this,permissions[1])
                !=PackageManager.PERMISSION_GRANTED){
                //request External storage permissions
                reQuestRunTimePermission(permissions);
        }
    }
        private void reQuestRunTimePermission(String[] permissions){

            ActivityCompat.requestPermissions(this, permissions,
                    PackageManager.PERMISSION_GRANTED); }

    private void setUpWidget() {

        //find Buttons
        mScaBT=(Button) findViewById(R.id.mScanBarCode);
        mGenerateBt=(Button) findViewById(R.id.mGenerate);
        mGenerateEDT=(EditText) findViewById(R.id.mBarCodeEditText);
        mBarcodeImage=(ImageView) findViewById(R.id.mBarCodeImage);

        //set Listener
        mGenerateBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get user input text
                String inputs=getUserInputs(v);

                if (inputs.isEmpty()){
                    return;
                }
                /**[otherwise]**/
                //start Generating QR Code
                generateBarCodes(inputs);

            }
        });

        mScaBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, BarCodeScanner.class));

            }
        });
    }

    private String getUserInputs(View v) {

        String userInputs= mGenerateEDT.getText().toString().trim();
        if (userInputs.isEmpty()){
            mGenerateEDT.setError("please enter text here to start Generating");
            return "";
        }
        Log.d(TAG, "startGeneratingBarCode: ");
//        Toast.makeText(this, "Text :"+userInputs, Toast.LENGTH_SHORT).show();
        return userInputs;
    }


/**
 * from [https://github.com/androidmads/QRGenerator] library
 * */
    private void generateBarCodes(String inputValue) {

        // Dimension for QR Code (could be any number from 1 to ..n )
        int smallerDimension = 3000;

        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        qrgEncoder.setColorBlack(Color.RED);

        //border/background color
        //qrgEncoder.setColorWhite(Color.BLUE);

        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap();

        // Setting Bitmap to ImageView
        mBarcodeImage.setImageBitmap(bitmap);
    }
}