package com.example.sjeong.pick;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setting = (Button) findViewById(R.id.setting);
        Button myPage = (Button) findViewById(R.id.myPage);
        Button target = (Button) findViewById(R.id.target);
        Button calculator = (Button) findViewById(R.id.calculator);
        Button deposit = (Button)findViewById(R.id.deposit);
        Button FAQ = (Button)findViewById(R.id.FAQ);


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        myPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyActivity.class);
                startActivity(intent);
            }
        });

        target.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                startActivity(intent);
            }
        });

        calculator.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CalculatorActivity.class);
                startActivity(intent);
            }
        });

        deposit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("Test", "click Test SearchActivity");

                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });

        FAQ.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), FAQActivity.class);
                startActivity(intent);
            }
        });
    }

}
