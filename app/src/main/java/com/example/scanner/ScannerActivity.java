package com.example.scanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class ScannerActivity extends AppCompatActivity implements DecoratedBarcodeView.TorchListener {

    private CaptureManager capture;                     //DecoratedBarcodeView 제어
    private DecoratedBarcodeView ScannerView;    //커스텀 스캐너 레이아웃을 위한 뷰

    private Spinner UserSpinner;                        //관리자 아이디 스피너
    private ImageButton LogoutBtn;                      //로그아웃 버튼
    private TextView KindCode;                          //바코드 종류 표시
    private ImageButton BarcodeToggle;                  //바코드 종류 선택
    private Button ViewInfoBtn;                         //바코드 정보 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_layout);

        UserSpinner = (Spinner)findViewById(R.id.spinner_manager);
        LogoutBtn = (ImageButton)findViewById(R.id.button_log_out);
        ViewInfoBtn = (Button)findViewById(R.id.view_info_barcode);

        DecoratedBarcodeView barcodeScannerView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }


    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

}