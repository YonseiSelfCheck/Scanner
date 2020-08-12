package com.example.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private Button qr_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TODO 이 메인에 로그인 구현해주세요 회원가입같은건 서버단에서 해도 될듯

        qr_btn = (Button) findViewById(R.id.goto_qr);

        qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QRScannerActivity.class);
                startActivity(intent);
            }
        });
    }



}