package com.example.sjeong.pick;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FindInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);

        FindFragment fragment = new FindFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.find_fragment, fragment);
        ft.commit();
    }
}
