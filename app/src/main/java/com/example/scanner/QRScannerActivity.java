package com.example.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class QRScannerActivity extends AppCompatActivity {

    CameraSource cameraSource;
    SurfaceView cameraSurface;
    String resultCode;
    TextView QRcodeInfo;
    Intent getIntent;
    private View progressView;

    ArrayList<String> barcode = new ArrayList<String>();

    int idx = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);
        getIntent = getIntent();
        getCameraPermission();
        QRcodeInfo = (TextView) findViewById(R.id.view_info_barcode);
        cameraSurface = (SurfaceView) findViewById(R.id.cameraSurface);
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        Log.d("NowStatus", "BarcodeDetector Build Complate");

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(29.8f)
                .setRequestedPreviewSize(1080, 1920)
                .setAutoFocusEnabled(true)
                .build();
        Log.d("NowStatus", "CameraSource Build Complete");
// Callback을 이용해서 SurfaceView를 실시간으로 Mobile Vision API와 연결
        cameraSurface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {   // try-catch 문은 Camera 권한획득을 위한 권장사항
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(cameraSurface.getHolder());  // Mobile Vision API 시작
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();    // SurfaceView가 종료되었을 때, Mobile Vision API 종료
                Log.d("NowStatus", "SurfaceView Destroyed and CameraSource Stopped");
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.d("NowStatus", "BarcodeDetector SetProcessor Released");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                HashSet<String> hash_barcode = new HashSet<String>(barcode);
                ArrayList<String> barcode_result = new ArrayList<String>(hash_barcode);
                String serial = "";
                if (barcodes.size() != 0) {
                    String barcodeContents = barcodes.valueAt(0).displayValue; // 바코드 인식 결과물
                    //TODO 일단 QR 읽었으니까 setText지우고 관리자정보+QR데이터+날짜 HashMap에 붙이고 Json으로 ㄱㄱ
                    QRcodeInfo.setText(barcodeContents);
                    barcode.add(barcodeContents);
                    Log.e("size", String.valueOf(barcode_result.size()));

                }
            }
        });
        QRcodeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 이거 HTTP로 쏠 부분
            }
        });
    }


    private boolean getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
                Toast.makeText(this, "카메라 사용을 위해 확인버튼을 눌러주세요!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        PackageManager.PERMISSION_GRANTED);
                return true;
            }
        }
    }
}