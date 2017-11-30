package com.example.sjeong.pick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상단바 색 변경
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("Loading error", e.toString());
            finish();
        }

        // 자동로그인 확인
        SharedPreferences preferences = getSharedPreferences("person", MODE_PRIVATE);
        String auto_login_id = preferences.getString("auto_login", "");
        Log.i("auto login", auto_login_id);

        Intent intent;
        if(!auto_login_id.equals("")) // 자동로그인 있는 경우
            intent = new Intent(SplashActivity.this, MainActivity.class);
        else
            intent = new Intent(SplashActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }
}
