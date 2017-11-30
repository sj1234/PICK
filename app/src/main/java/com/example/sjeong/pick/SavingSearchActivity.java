package com.example.sjeong.pick;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SavingSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_search);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        SearchDetailSavingFragment searchDetailSavingFragment = new SearchDetailSavingFragment();
        ft.replace(R.id.fragment, searchDetailSavingFragment);
        ft.commit();
    }
}
