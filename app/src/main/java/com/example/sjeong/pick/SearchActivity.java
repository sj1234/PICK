package com.example.sjeong.pick;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        TopFragment top = new TopFragment();
        SearchFragment search = new SearchFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_down, top);
        ft.replace(R.id.fragment_up, search);
        ft.commit();

        Log.i("Test", "Test SearchActivity");
    }
}
