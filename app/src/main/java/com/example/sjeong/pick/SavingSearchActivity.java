package com.example.sjeong.pick;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.sjeong.pick.Saving.SearchDetailSavingFragment;

public class SavingSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_search);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#ffffff"));

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        SearchDetailSavingFragment searchDetailSavingFragment = new SearchDetailSavingFragment();
        ft.replace(R.id.fragment, searchDetailSavingFragment);
        ft.commit();
    }
}
