package com.example.sjeong.pick;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SearchDepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dep);

        LogoFragment logo = new LogoFragment();
        SearchDepFragment search = new SearchDepFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_down, logo);
        ft.replace(R.id.fragment_up, search);
        ft.commit();

        Log.i("Test", "Test SearchActivity");
    }
}
