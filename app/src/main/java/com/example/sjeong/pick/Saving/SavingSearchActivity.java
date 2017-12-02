package com.example.sjeong.pick.Saving;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.sjeong.pick.R;

public class SavingSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_search);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        SearchSimpleSavingFragment searchSimpleSavingFragment = new SearchSimpleSavingFragment();
        ft.replace(R.id.fragment_search, searchSimpleSavingFragment, "SearchSimpleSaving");
        ft.commit();
    }
}
