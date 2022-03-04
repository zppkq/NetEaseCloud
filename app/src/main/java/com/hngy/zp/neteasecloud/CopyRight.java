package com.hngy.zp.neteasecloud;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class CopyRight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_copy_right);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(com.hngy.zp.neteasecloud.CopyRight.this, MainActivity.class));
                finish();
            }
        }.start();
    }
}