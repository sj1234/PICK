package com.example.sjeong.pick;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상단바 색 변경
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#d4e9fa"));

        setContentView(R.layout.activity_main);

        // 로그아웃 버튼
        ImageButton menu_logout = (ImageButton) findViewById(R.id.menu_logout);
        menu_logout.setOnClickListener(this);
        // 설정 버튼
        ImageButton menu_setting = (ImageButton) findViewById(R.id.menu_setting);
        menu_setting.setOnClickListener(this);
        // 예금
        LinearLayout menu_dep = (LinearLayout)findViewById(R.id.menu_dep);
        menu_dep.setOnClickListener(this);
        // 적금
        LinearLayout menu_sav = (LinearLayout)findViewById(R.id.menu_sav);
        menu_sav.setOnClickListener(this);
        // 계산기
        LinearLayout menu_cac = (LinearLayout)findViewById(R.id.menu_cac);
        menu_cac.setOnClickListener(this);
        // 목표
        LinearLayout menu_goal = (LinearLayout)findViewById(R.id.menu_goal);
        menu_goal.setOnClickListener(this);
        // 관심
        LinearLayout menu_int = (LinearLayout)findViewById(R.id.menu_int);
        menu_dep.setOnClickListener(this);
        // faq
        LinearLayout menu_faq = (LinearLayout)findViewById(R.id.menu_faq);
        menu_faq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.menu_logout: // 로그아웃
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("LOGOUT")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                // 자동로그인 정보 및 로그인 ID 정보 지우기
                                SharedPreferences preferences = getSharedPreferences("person", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.remove("auto_login");
                                editor.remove("id");
                                editor.commit();

                                // main 화면으로 이동
                                Intent main = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(main);
                                finish();
                            }
                        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.menu_setting: // 설정
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;
            case R.id.menu_dep: // 예금
                Intent dep = new Intent(MainActivity.this, SearchDepActivity.class);
                startActivity(dep);
                break;
            case R.id.menu_sav: // 적금
                Intent sav = new Intent(MainActivity.this, SavingSearchActivity.class);
                startActivity(sav);
                break;
            case R.id.menu_cac: // 계산기
                Intent cac = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(cac);
                break;
            case R.id.menu_goal: // 목표
                Intent goal = new Intent(MainActivity.this, GoalActivity.class);
                startActivity(goal);
                break;
            case R.id.menu_int: // 관심
                //Intent interest = new Intent(MainActivity.this, MyActivity.class);
                //startActivity(interest);
                break;
            case R.id.menu_faq: // faq
                Intent faq = new Intent(MainActivity.this, FAQActivity.class);
                startActivity(faq);
                break;
        }
    }
}
